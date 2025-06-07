**TaskFlow** - это система для менеджеров проектов, которая позволит просмотреть общую информацию о проекте и отдельную, о каждом из программистов.
Работа приложения построена на интеграции со внешними сервисами, а именно GitHub API и Jira API. Именно оттуда мы берем всю информацию, и в далнейшем используем ее.
Из-за огромного количества настроек, которые можно применить внутри Jira и GitHub, в TaskFlow подтягивается только базовая информация для всех репозиториев(кол-во коммитов, добавленных строк и тд)  
и досок в jira(общее кол-во задач, количество активных задач(задачи в которых отсутствует время окончания), время выполнения задачи и тд).

**Cразу стоит обговорить соглашения и правила, которых мы предерживались при разработке:**
1) Сопоставление email пользователей в GitHub и Jira(подразумевается, что учетные записи в этих сервисах привязаны к одной рабочей почте)
2) Весь код находится в одном монорепозитории (одна кодовая база для облегчения разработки и уменьшения количество запросов к внешним api)
3) Разделение между пользователями входящими в систему и пользователями из Jira и GitHub(это 2 разных типа пользователей, не имеющие никаких пересечений)  



**ИНСТРУКЦИЯ К ПЕРВОМУ ЗАПУСКУ**

 1)**Создания базы данных**  
 В папке database лежит файл shema.sql, в котором находится исходный код для создания всех таблиц. Необходимо создать базу данных любым удобным для вас способом
(консольной утилитой или GUI-клиенты поддерживающией PostgreSQL(например pgAd))

2)**Настройка application.yaml файлов**  
Во всех сервисах(auth-service, data-update-service, stat-service) находится папка resources c файлом application.yaml.
В данном файле задаются базовые настройки для корректной работы приложения. Его необходимо заполнить в каждом из сервисов.
Ниже будут представлены данные, которые необходимо заполнить своей информацией для корректной работы приложения.

**auth-service:**
```yaml
datasource:
    url: jdbc:postgresql://localhost:5432/postgres
    password: postgres
    username: postgres
```
________________________________________________________________________________________
**Обратите внимание:**  
В следующих двух файлах используется настройка подключения к rabbitmq.
Эта технология отвечает за доставку сообщений об обновлении данных между сервисами, чтобы пользователи всегда видели актуальную статистику.  

Для полноценного тестирования автоматического обновления данных необходимы RabbitMQ и Erlang.   

**Скачать их можно по ссылкам:**  
RabbitMQ (https://www.rabbitmq.com/docs/download)  
Erlang (https://www.rabbitmq.com/docs/which-erlang)  
На указанном сайте есть инструкции по установке и запуску RabbitMQ.

**Однако**:  
Функциональность интеграции с RabbitMQ в коде по умолчанию закомментирована.
Это значит, что для базового запуска и тестирования вашего приложения **_устанавливать RabbitMQ не обязательно_**.
Без него обновление данных не происходит автоматически, но вы всегда можете получить актуальные данные, просто перезапустив оба сервиса.
Если же вы хотите протестировать автоматическое обновление — раскомментируйте соответствующий код и настройте/запустите RabbitMQ согласно описанию выше.  

**Файлы в которыx нужно исправить код для RabbitMQ:**  
```java
//data-update-service -> sheduler.UpdatesScheduler
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
```

```java
//data-update-service -> service.InitialLoader
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
```

```java
//stat-service -> service.RabbitReceiver
@Service
@RequiredArgsConstructor
public class RabbitReceiver {
     private final StatisticService statisticService;

     @PostConstruct//закомментировать 
    //@RabbitListener(queues = {"mainQueue"})
    public void handleEvent() {
        //String[] words = message.split(" ");
        //Long repoId = Long.parseLong(words[0]);

        // Обновляем статистику коммитов
        statisticService.updateCommitStatistics(1L);//использовать repoId вместо 1

        // Обновляем статистику задач
        statisticService.updateIssueStatistics(1L);//использовать repoId вместо 1
    }
}
```
________________________________________________________________________________________

**data-update-service:**
```yaml
  datasource:
    username: postgres
    password: postgres
    url: jdbc:postgresql://localhost:5432/postgres

  rabbitmq:
    username: quest
    host: localhost
    password: quest
    port: 5672

queue:
  name: mainQueue

github:
  token: your_token
  baseurl: https://api.github.com
  repos: your_repos
  owner: owner_this_repo

jira:
  token: your_token
  baseurl: https://jiragitstat.atlassian.net/rest/api/2
  email: verification_email
```

**stat-service:**
```yaml
  rabbitmq:
    username: quest
    host: localhost
    password: quest
    port: 5672

  datasource:
    password: postgres
    username: postgres
    url: jdbc:postgresql://localhost:5432/postgres
```

3) **Добавление первого пользователя**  
   Для успешного прохождения аутентификации в базу необходимо положить email и password первого пользователя системы. 
   Для этого в auth-service есть специальный endpoint. Для доступа к нему необходимо запустить данный сервис и отправить post запрос используя
   командную строку с утилитой curl(как правило предуставновелнную на все ОС) или любой другой почтальен(Postman, Insomnia и т.д)

```cmd
curl -X POST http://localhost:8083/register -H "Content-Type:application/json" -d
"{\"email\": \"nastya@gmail.com\", \"password\": \"nastya\", \"roles\": \"ROLE_USER\"}"
```
Измените значениия email и password на свои.
___________________________________________________________________
**Прошу обратить внимание на то, что добавление данных других пользователей также возможно только по этому endpoint**. 
При занесении данных пользователя напрямую в бд пароль будет сохранен в незакодированном виде, поэтому он не будет подходить при попытке войти в приложение.
___________________________________________________________________
4)**Запуск остальных сервисов в фронтенда**  
Запускаем data-update-service и stat-service, после чего переходим к визуальной составляющей проекта.  
Все файлы, необходимые для frontend`a, находятся в папке с аналогичным называнием. 
Для их запуска мы использвали visual studio code с расширением live server. Необходимо открыть данную папку в vs code, и после, из файла start_window.html нажать кнопку "Go live"(которая появится после установки расширения live server), расположенную снизу справа.
Вас автоматически перенесет на форму входа.   

**ПРИЛОЖЕНИЕ ГОТОВО К ИСПОЛЬЗОВАНИЮ!!!**



  













