$(document).ready(function () {

    // --- Overall Status & Donut Chart ---
    let statusDonutChart = null;
    function fetchOverallStatus() {
        fetch('/api/status/overall')
            .then(response => response.json())
            .then(data => {
                // Update Status Cards
                const container = $('#overall-status-cards');
                container.empty(); 

                const createCard = (title, value, icon, colorClass) => `
                    <div class="mb-4">
                        <div class="card bg-dark-2 text-white h-100">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h5 class="card-title">${title}</h5>
                                        <p class="card-text fs-4">${value}</p>
                                    </div>
                                    <i class="fs-2 ${icon} ${colorClass}"></i>
                                </div>
                            </div>
                        </div>
                    </div>`;

                container.append(createCard('Total', data.totalInterfaces, 'bi-hdd-stack', 'text-primary'));
                container.append(createCard('Success', data.success, 'bi-check-circle', 'text-success'));
                container.append(createCard('Error', data.error, 'bi-exclamation-triangle', 'text-danger'));
                container.append(createCard('Retray', data.retray, 'bi-question-circle', 'text-warning'));
				container.append(createCard('Etc', (data.totalInterfaces - data.success), 'bi-question-circle', 'text-warning'));
				
                if ($('link[href*="bootstrap-icons"]').length === 0) {
                    $('head').append('<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">');
                }

                // Update Donut Chart
                const ctx = document.getElementById('statusDonutChart').getContext('2d');
                const chartData = {
                    labels: ['Success', 'Error', 'Retray'],
                    datasets: [{
                        data: [data.success, data.error, data.retray],
                        backgroundColor: ['#198754', '#dc3545', '#ffc107'],
                        borderColor: '#2c2c2c',
                        borderWidth: 3
                    }]
                };

                if (statusDonutChart) {
                    statusDonutChart.data = chartData;
                    statusDonutChart.update();
                } else {
                    statusDonutChart = new Chart(ctx, {
                        type: 'doughnut',
                        data: chartData,
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            plugins: {
                                legend: {
                                    position: 'bottom',
                                    labels: {
                                        color: '#ccc',
                                        padding: 15,
                                        font: { size: 14 }
                                    }
                                }
                            }
                        }
                    });
                }
            })
            .catch(error => console.error('Error fetching overall status:', error));
    }

    // --- Service Error Log Table ---
    let errorTable = null;
    function initializeErrorTable() {
        errorTable = $('#errorLogTable').DataTable({
            "processing": true,
            "serverSide": false,
            "ajax": {
                "url": "/api/errors",
                "dataSrc": "data"
            },
            "columns": [
                { "data": "id" },
                { "data": "service" },
				{ "data": "errorDesc" },
                { "data": "message" },
                { "data": "timestamp" }
            ],
            "responsive": true,
            "lengthChange": false,
            "pageLength": 10,
            "language": {
                "search": "",
                "searchPlaceholder": "Search logs...",
                "paginate": { "previous": "‹", "next": "›" },
                "emptyTable": "No errors to display",
                "info": "Showing _START_ to _END_ of _TOTAL_ entries"
            }
        });

        $('#errorLogTable tbody').on('click', 'tr', function () {
            const rowData = errorTable.row(this).data();
            if (!rowData) return;

            const errorId = rowData.id;
            fetch(`/api/error-log/${errorId}`)
                .then(response => response.json())
                .then(logData => {
                    $('#errorLogContent').text(logData.errorLog || 'No details available.');
                    const errorModal = new bootstrap.Modal(document.getElementById('errorLogModal'));
                    errorModal.show();
                })
                .catch(error => {
                    console.error('Error fetching error log details:', error);
                    $('#errorLogContent').text('Failed to load log details.');
                    const errorModal = new bootstrap.Modal(document.getElementById('errorLogModal'));
                    errorModal.show();
                });
        });
    }

    // --- Schedule Log Table ---
    let scheduleTable = null;
    function initializeScheduleTable() {
        scheduleTable = $('#scheduleLogTable').DataTable({
            "processing": true,
            "serverSide": false,
            "ajax": {
                "url": "/api/schedules",
                "dataSrc": "data"
            },
            "columns": [
                { "data": "scheduleId" },
                { "data": "interfaceName" },
                { "data": "status" },
                { "data": "executionTime" },
                { "data": "duration" }
            ],
            "responsive": true,
            "lengthChange": false,
            "pageLength": 10,
            "language": {
                "search": "",
                "searchPlaceholder": "Search schedules...",
                "paginate": { "previous": "‹", "next": "›" },
                "emptyTable": "No schedules to display",
                "info": "Showing _START_ to _END_ of _TOTAL_ entries"
            }
        });
    }

    // Initial data load
    fetchOverallStatus();
    initializeErrorTable();
    initializeScheduleTable();

    // Set intervals to refresh data periodically
    setInterval(fetchOverallStatus, 15000); // Refresh cards and donut chart
    setInterval(() => errorTable.ajax.reload(null, false), 60000);
    setInterval(() => scheduleTable.ajax.reload(null, false), 60000); // Refresh schedule table

});
