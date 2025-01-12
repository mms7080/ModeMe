// 테이블 데이터를 바탕으로 매출 데이터 생성
       const salesData = [
           { date: '2025-01-09', category: 'top', amount: 18000 },
           { date: '2025-02-08', category: 'bottom', amount: 40000 },
           { date: '2025-01-10', category: 'top', amount: 22000 },
           { date: '2025-02-08', category: 'outer', amount: 5000 }
       ];

       // 월별 매출 데이터 집계
       const monthlyData = salesData.reduce((acc, cur) => {
           const month = cur.date.slice(0, 7); // 'YYYY-MM' 형식으로 추출
           acc[month] = (acc[month] || 0) + cur.amount;
           return acc;
       }, {});

       // 카테고리별 매출 데이터 집계
       const categoryData = salesData.reduce((acc, cur) => {
           const category = cur.category; // 카테고리
           acc[category] = (acc[category] || 0) + cur.amount;
           return acc;
       }, {});

       // Chart.js 데이터 변환
       const months = [
           '2025-01', '2025-02', '2025-03', '2025-04', 
           '2025-05', '2025-06', '2025-07', '2025-08', 
           '2025-09', '2025-10', '2025-11', '2025-12'
       ];
       const chartDataMonthly = months.map(month => monthlyData[month] || 0);

       const categories = ['outer', 'top', 'bottom', 'ops', 'acc'];
       const chartDataCategory = categories.map(category => categoryData[category] || 0);

       // 월별 차트
       const ctxMonthly = document.getElementById('salesChartMonthly').getContext('2d');
       const salesChartMonthly = new Chart(ctxMonthly, {
           type: 'line',
           data: {
               labels: months.map(m => m.replace('2025-', '') + '월'),
               datasets: [{
                   label: '매출 금액',
                   data: chartDataMonthly,
                   backgroundColor: 'rgba(54, 162, 235, 0.2)',
                   borderColor: 'rgba(54, 162, 235, 1)',
                   borderWidth: 2,
                   fill: true,
                   tension: 0.4
               }]
           },
           options: {
               responsive: true,
               plugins: {
                   legend: { position: 'top' },
                   title: { display: true, text: '월별 매출 금액 (2025)' }
               },
               scales: {
                   y: { beginAtZero: true }
               }
           }
       });

       // 카테고리별 차트
       const ctxCategory = document.getElementById('salesChartCategory').getContext('2d');
       const salesChartCategory = new Chart(ctxCategory, {
           type: 'bar',
           data: {
               labels: categories.map(c => c.toUpperCase()), // 카테고리 대문자로 표시
               datasets: [{
                   label: '매출 금액',
                   data: chartDataCategory,
                   backgroundColor: [
                       'rgba(255, 99, 132, 0.2)',
                       'rgba(54, 162, 235, 0.2)',
                       'rgba(255, 206, 86, 0.2)',
                       'rgba(75, 192, 192, 0.2)',
                       'rgba(153, 102, 255, 0.2)'
                   ],
                   borderColor: [
                       'rgba(255, 99, 132, 1)',
                       'rgba(54, 162, 235, 1)',
                       'rgba(255, 206, 86, 1)',
                       'rgba(75, 192, 192, 1)',
                       'rgba(153, 102, 255, 1)'
                   ],
                   borderWidth: 2
               }]
           },
           options: {
               responsive: true,
               plugins: {
                   legend: { position: 'top' },
                   title: { display: true, text: '카테고리별 매출 금액 (2025)' }
               },
               scales: {
                   y: { beginAtZero: true }
               }
           }
       });