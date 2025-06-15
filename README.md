**TaskFlow** - это система для менеджеров проектов, которая позволит просмотреть общую информацию о проекте и отдельную, о каждом из разработчиков.
Работа приложения построена на интеграции со внешними сервисами, а именно GitHub API и Jira API. Именно оттуда мы берем всю информацию, и в далнейшем используем ее.  
Из-за огромного количества настроек, которые можно применить внутри Jira и GitHub, во FlowTask подтягивается только базовая информация для всех репозиториев(кол-во коммитов, добавленных строк и тд)
и досок в jira(общее кол-во задач, количество активных задач(задачи в которых отсутствует время окончания), время выполнения задачи и тд).

**Cразу стоит обговорить соглашения и правила, которых мы предерживались при разработке:**
1) Сопоставление email пользователей в GitHub и Jira(подразумевается, что учетные записи в этих сервисах привязаны к одной рабочей почте)
2) Весь код находится в одном монорепозитории (одна кодовая база для облегчения разработки и уменьшения количество запросов к внешним api)
3) Разделение между пользователями входящими в систему и пользователями из Jira и GitHub(эта 2 разных типа пользователей, не имеющие никаких пересечений)

**ИНСТРУКЦИЯ К ПЕРВОМУ ЗАПУСКУ**
1) **Создание базы данных**  
В корне проекта есть папка "_database_", где лежит файл "_shema.sql_", который содержит исходный код базы данных. 
Любым удобным для вас способом необходимо применить данный скрипт, например можно воспользоваться любым графическим интерфейсом (например, DBeaver, pgAdmin и др.), 
ипортировав туда данный файл.

2) **Заполнение application.yaml файлов**   
Данный файл есть в каждом из трех сервисов: auth-servcie, data-update-service и stat-service. 
Пройдемся по-каждому из них, и посмотрим, где необходимо изменить данные на свои.
- auth-service
```yaml
datasource:
    url: jdbc:postgresql://localhost:5432/postgres
    password: postgres
    username: postgres

server:
  port: 65535
```

- data-udpate-service
```yaml
server:
  port: 65534

  datasource:
    username: postgres
    password: postgres
    url: jdbc:postgresql://localhost:5432/postgres 

github:
  token: your_token
  baseurl: https://api.github.com
  repos: your_repos
  owner: owner_repos

jira:
  token: your_token
  baseurl: https://jiragitstat.atlassian.net/rest/api/2
  email: creater-dashbord_email
```  

- stat-service
```yaml
  datasource:
    password: postgres
    username: postgres
    url: jdbc:postgresql://localhost:5432/postgres

server:
  port: 65533
```

________________________________________________________________________
**Обратите внимание!!!**  
- В данных application.yaml файлах перечислены настройки, которые 
стоит изменить на подходящие для вас значеия. 
Так для подключения к ранее созданной базе данных необходимо прописать 
username, password и url именно для нее.  
Остальные настройки, неупомянутые здесь, но прописанные в файле, можно оставить без изменений.
- Вы могли заметить, что в настройках data-update-service и stat-service присутствует RabbitMQ.
Это инструмент, с помощью которого осуществляется обмен сообщениями между этими сервисами.
К сожалению, для его использования необходимо установить Erlang и RabbitMQ(https://www.erlang.org/downloads и https://www.rabbitmq.com/),
поэтому часть кода, связанная с этой функциональностью, закомментирована:

```java
// data-udpate-service/sheduler 
@Component
@RequiredArgsConstructor
@EnableScheduling
public class UpdatesScheduler {
    private final UserMappingLoader userMappingUpdater;
    private final CommitUpdater commitUpdater;
    private final IssueUpdater issueUpdater;
    private final MessageSender messageSender;

    //@Scheduled(cron = "0 */2 * * * *")
    public void loadUpdate(){
       userMappingUpdater.loadUserMapping();
        issueUpdater.updateIssues();
        commitUpdater.updateCommits();

        System.out.println("Данные обновились...");
        messageSender.sendMassage();
    }
}

// data-update-service/service
@Service
@RequiredArgsConstructor
public class  InitialLoader {
    private final IssueLoader issueLoader;
    private final UserMappingLoader userMappingInitializer;
    private final CommitLoader commitLoader;
    private final RepoLoader repoInitializer;
   // private final Queue queue;
   // private final RabbitAdmin rabbitAdmin;
    private final MessageSender messageSender;

    @PostConstruct
    public void saveInitialInformation(){
        //rabbitAdmin.declareQueue(queue);

        repoInitializer.initRepository();
        userMappingInitializer.loadUserMapping();
        issueLoader.loadIssues();
        commitLoader.loadCommits();

        System.out.println("Данные обновились...");
       // messageSender.sendMassage();
    }

}

// stat-service/service
@Service
@RequiredArgsConstructor
public class RabbitReceiver {
     private final StatisticService statisticService;

     @PostConstruct
    //@RabbitListener(queues = {"mainQueue"})
    public void handleEvent() {
        //String[] words = message.split(" ");
        //Long repoId = Long.parseLong(words[0]);

        // Обновляем статистику коммитов
        statisticService.updateCommitStatistics(1L);//repoId

        // Обновляем статистику задач
        statisticService.updateIssueStatistics(1L);////repoId
    }
}

```
В такой реализации, для обновления данных необходимо просто перезапустить все сервисы.  
В случае установки RabbitMq стоит раскомменитровать код. 
________________________________________________________________________

3) **Добавлеение пользователя системы**  
Для добавления первого пользователя вы можете использовать любой удобный инструмент для отправки HTTP-запросов, например, Postman, Insomnia 
или командную строку с утилитой curl (обычно предустановлена в большинстве ОС).

**Пример с использованием curl в командной строке:**
```bash
curl -X POST http://localhost:8083/register \
  -H "Content-Type: application/json" \
  -d "{\"email\": \"your_email@example.com\", \"password\": \"your_password\", \"roles\": \"ROLE_USER\"}"
```
Где:  
email — ваш email,  
password — желаемый пароль,  
roles — роль пользователя (в данной версии приложения ни на что не влияет, рекомендуется оставить ROLE_USER).  
Замените значения на свои. После успешного выполнения запроса пользователь будет добавлен в систему.  
Если удобнее, то тот же запрос можно отправить через GUI-интерфейсы, такие как Postman или Insomnia, указав URL, метод POST,   
передав заголовок Content-Type: application/json и тело запроса в формате JSON (см. пример выше).  

________________________________________________________________________
Обратите внимание, что в случае добавления записи напрямую в базу данных, пароль будет сохранен в незашфровоанном виде, что не является корректным.  
Такие пользователи не смогут пройти авторизацию. 
________________________________________________________________________

4) **Запуск всех сервисов** 
Запустить все 3 сервиса.

5) **Запуск frontend части.**  
В корне проекта есть папка "_fronted_" со всеми необходимыми файлами. Самым простым способом запуска является использования Visual Studio Code 
с расширением "_Live Server_". После открытия папки и установки соответствующего расширения остается нажать появившуюся кнопку "_Go Live_" в нижнем 
правом углу внутри файла **start_window.html**

ПРИЛОЖЕНИЕ ГОТОВО К ЗАПУСКУ!
 
