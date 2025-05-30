const API_BASE_URL = 'http://localhost:65533'; 

const buttons = document.querySelectorAll('.menu-button');
const currentSection = document.getElementById('current-section');
const sectionTitle = document.getElementById('section-title');
const dynamicContent = document.querySelector('.dynamic-content');

// Загрузка статистики
async function loadStats() {
  try {
    const [doneIssues, addedLines, deletedLines, commits, totalAvgLines] = await Promise.all([
      fetchData('/api/stat/doneIssues'),
      fetchData('/api/stat/addedLines'),
      fetchData('/api/stat/deletedLines'),
      fetchData('/api/stat/commits'),
      fetchData('/api/stat/totalAvgLines')
    ]);

    console.log('Статистика:', { doneIssues, addedLines, deletedLines, commits, totalAvgLines }); // Логируем полученные данные

    if (doneIssues !== null) document.getElementById('done-issues').textContent = doneIssues;
    else console.error('Не удалось получить данные по задачам');
    
    if (addedLines !== null) document.getElementById('added-lines').textContent = addedLines;
    else console.error('Не удалось получить данные по добавленным строкам');
    
    if (deletedLines !== null) document.getElementById('deleted-lines').textContent = deletedLines;
    else console.error('Не удалось получить данные по удалённым строкам');
    
    if (commits !== null) document.getElementById('commits-total').textContent = commits;

    if (totalAvgLines !== null) document.getElementById('avg-lines-total').textContent = totalAvgLines;

    else console.error('Не удалось получить данные по коммитам');
  } catch (error) {
    console.error('Ошибка загрузки статистики:', error);
    alert('Не удалось загрузить статистику');
  }
}
// Функция для выполнения запросов к API
async function fetchData(endpoint) {
  try {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jwt')}`
      }
    });
    
    if (!response.ok) {
      throw new Error('Ошибка при получении данных');
    }
    
    return await response.json();
  } catch (error) {
    console.error('Ошибка:', error);
    return null;
  }
}

// Функция для форматирования времени
function formatTime(hours) {
  return `${hours.toFixed(1)} ч`;
}

const sections = {
  'Статистика': `
 
    <div class="stats-section">
      <section class="stats-block">
        <h3 class="block-title">Задачи</h3>
        <div class="cards">
          <div class="card">
            <p class="card-title">Решенные задачи</p>
            <p class="card-value" id="done-issues">0</p>
            
          </div>
        </div>
        <canvas id="issues-chart" width="400" height="200"></canvas>
      </section>

      <section class="stats-block">
        <h3 class="block-title">Программный код</h3>
        <div class="cards">
          <div class="card">
            <p class="card-title">Добавлено строк</p>
            <p class="card-value" id="added-lines">0</p>
            
          </div>
          <div class="card">
            <p class="card-title">Удалено строк</p>
            <p class="card-value" id="deleted-lines">0</p>
            
          </div>
          <div class="card">
            <p class="card-title">Коммитов</p>
            <p class="card-value" id="commits-total">0</p>
            
          </div>

          <div class="cards">
            <div class="card">
            <p class="card-title">Среднее количество строк в коммите</p>
            <p class="card-value" id="avg-lines-total">0</p>
            </div>
          </div>
        </div>
      </section>
    </div>
  `,
  'Команда': `
    <div class="stats-section">
      <section class="stats-block">
        <div class="search-container">
          <input type="email" id="employee-search" class="search-input" placeholder="Поиск по email...">
          <button id="search-button" class="search-button">Найти</button>
        </div>
        
        <div class="employee-info">
          <h3 class="block-title" id="employee-name">Введите email сотрудника</h3>
          
          <div class="cards">
            <div class="card">
              <p class="card-title">Решенные задачи</p>
              <p class="card-value" id="done-issues-employee">0</p>
              
            </div>
            <div class="card">
              <p class="card-title">Среднее время</p>
              <p class="card-value" id="avg-time-employee">0 ч</p>
              
            </div>
          </div>
          
          <div class="cards" style="margin-top: 20px;">
            <div class="card">
              <p class="card-title">Кол-во коммитов</p>
              <p class="card-value" id="commits-employee">0</p>
              
            </div>
            <div class="card">
              <p class="card-title">Добавлено строк</p>
              <p class="card-value" id="added-lines-employee">0</p>
              
            </div>

              <div class="card">
    <p class="card-title">Среднее количество строк в коммите</p>
    <p class="card-value" id="avg-lines-employee">0</p>
  </div>
          </div>

        </div>
      </section>
    </div>
  `,
  'Рейтинг': `
    <div class="stats-section rating-section">
      <section class="stats-block">
        <h3 class="block-title">Рейтинг сотрудников</h3>
        
        <div class="sort-controls">
          <button class="sort-button active" data-sort="doneIssues">Количество задач</button>
          <button class="sort-button" data-sort="commits">Количество коммитов</button>
          <button class="sort-button" data-sort="addedLines">Добавлено строк</button>
        </div>
        
        <div class="rating-table">
          <div class="table-header">
            <div class="header-cell">Сотрудник</div>
            <div class="header-cell value-cell">Значение</div>
          </div>
          
          <div class="table-body" id="rating-body"></div>
        </div>
      </section>
    </div>
  `
};
// Загрузка статистики
async function loadStats() {
  const [doneIssues, addedLines, deletedLines, commits, totalAvgLines] = await Promise.all([
    fetchData('/api/stat/doneIssues'),
    fetchData('/api/stat/addedLines'),
    fetchData('/api/stat/deletedLines'),
    fetchData('/api/stat/commits'),
    fetchData('/api/stat/totalAvgLines')

  ]);

  if (doneIssues !== null) document.getElementById('done-issues').textContent = doneIssues;
  if (addedLines !== null) document.getElementById('added-lines').textContent = addedLines;
  if (deletedLines !== null) document.getElementById('deleted-lines').textContent = deletedLines;
  if (commits !== null) document.getElementById('commits-total').textContent = commits;
  if (totalAvgLines !== null) document.getElementById('avg-lines-total').textContent = totalAvgLines;
}

// Загрузка данных сотрудника
async function loadEmployeeData(email) {
  try {
    const [doneIssues, avgTime, commits, addedLines, avgLines] = await Promise.all([
      fetchData(`/api/stat/doneIssues/${email}`),
      fetchData(`/api/stat/avgTimeIssues/${email}`),
      fetchData(`/api/stat/commits/${email}`),
      fetchData(`/api/stat/addedLines/${email}`),
      fetchData(`/api/stat/totalAvgLines/${email}`)
    ]);

    document.getElementById('employee-name').textContent = email;
    if (doneIssues !== null) document.getElementById('done-issues-employee').textContent = doneIssues;
    if (avgTime !== null) document.getElementById('avg-time-employee').textContent = formatTime(avgTime);
    if (commits !== null) document.getElementById('commits-employee').textContent = commits;
    if (addedLines !== null) document.getElementById('added-lines-employee').textContent = addedLines;
    if (avgLines !== null) document.getElementById('avg-lines-employee').textContent = avgLines;
    
    return true;
  } catch (error) {
    console.error('Ошибка загрузки данных:', error);
    alert('Сотрудник с таким email не найден');
    return false;
  }
}

//график для страницы статистики
async function renderDoneIssuesChart() {
  try {
    const response = await fetch("http://127.0.0.1:65533/api/stat/doneIssueOnWeek");
    const data = await response.json();

    const labels = data.map(([date, _]) => {
      const d = new Date(date);
      return d.toLocaleDateString('ru-RU', { day: '2-digit', month: '2-digit' }); // "21.04"
    });

    const values = data.map(([_, count]) => count);

    const ctx = document.getElementById('issues-chart').getContext('2d');

    new Chart(ctx, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: 'Решённые задачи',
          data: values,
          backgroundColor: '#EDEEFC',
          borderColor: '#EDEEFC',
          borderWidth: 1,
          borderRadius: 5,
        }]
      },
      options: {
        responsive: true,
        plugins: {
          title: {
        display: true,
        text: 'Решённые задачи по дням недели',
        align: 'start', // <-- выравнивание по левому краю
        color: '#353535',
        font: {
          size: 18,
          family: 'Cygre', 
          
        },
        padding: {
          bottom: 40,
          top: 10

    }
      },
          legend: { display: false },
          tooltip: {
            callbacks: {
              label: (tooltipItem) => `Решено задач: ${tooltipItem.raw}`
            }
          }
        },
        scales: {
          x: {
        grid: {
          display: false // убирает вертикальную сетку
        }
      },
          y: {
            grid: {
          display: false // убирает горизонтальную сетку
        },
            beginAtZero: true,
            ticks: {
              stepSize: 1
            }
          }
        }
      }
    });

  } catch (error) {
    console.error('Ошибка при загрузке данных для графика:', error);
  }
}

// Вызов функции после загрузки страницы
window.addEventListener('DOMContentLoaded', () => {
  renderDoneIssuesChart();
});



//рейтинг
async function loadRating(sortBy) {
  try {
    console.log('Загружаем список пользователей...');
    
    // Получаем список email-ов
    const usersResponse = await fetch(`${API_BASE_URL}/api/stat/users`);
    if (!usersResponse.ok) {
      throw new Error(`Ошибка при получении списка пользователей: ${usersResponse.status}`);
    }
    const usersEmails = await usersResponse.json();

    // Получаем метрики для каждого email
    const usersWithMetrics = await Promise.all(usersEmails.map(async (email) => {
      let value = 0;
      switch(sortBy) {
        case 'doneIssues':
          value = await fetchData(`/api/stat/doneIssues/${email}`);
          break;
        case 'commits':
          value = await fetchData(`/api/stat/commits/${email}`);
          break;
        case 'addedLines':
          value = await fetchData(`/api/stat/addedLines/${email}`);
          break;
      }
      return { email, value: value || 0 };
    }));

    // Сортируем по убыванию
    usersWithMetrics.sort((a, b) => b.value - a.value);

    // Отрисовываем рейтинг
    const container = document.getElementById('rating-body');
    container.innerHTML = '';
    usersWithMetrics.forEach(user => {
      const row = document.createElement('div');
      row.className = 'table-row';
      row.innerHTML = `
        <div class="row-cell">${user.email}</div>
        <div class="row-cell value-cell">${user.value}</div>
      `;
      container.appendChild(row);
    });
  } catch (error) {
    console.error('Ошибка загрузки рейтинга:', error);
    alert('Не удалось загрузить рейтинг');
  }
}


// Обработчики событий
buttons.forEach(button => {
  button.addEventListener('click', () => {
    buttons.forEach(btn => btn.classList.remove('active'));
    button.classList.add('active');

    const section = button.getAttribute('data-section');
    currentSection.textContent = section;
    sectionTitle.textContent = `${section} по проекту`;

    dynamicContent.innerHTML = sections[section];
    
    // Загружаем данные для текущего раздела
    if (section === 'Статистика') {
      loadStats();
      renderDoneIssuesChart();
    } else if (section === 'Рейтинг') {
      loadRating('doneIssues');
    }
  });
});

document.addEventListener('click', async (e) => {
  if (e.target.id === 'search-button') {
    const emailInput = document.getElementById('employee-search');
    const email = emailInput.value.trim();
    
    if (!email) {
      alert('Введите email сотрудника');
      return;
    }
    
    await loadEmployeeData(email);
  }
  
  if (e.target.classList.contains('sort-button')) {
    document.querySelectorAll('.sort-button').forEach(btn => {
      btn.classList.remove('active');
    });
    e.target.classList.add('active');
    loadRating(e.target.dataset.sort);
  }
});

// Инициализация при загрузке
document.addEventListener('DOMContentLoaded', () => {
  loadStats();
});