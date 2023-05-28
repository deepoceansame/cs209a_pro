function createChart22(data) {

    // 1 3 24 168 720 1440
    var yval = [0, 0, 0, 0, 0, 0, 0]
    var thers = [1, 3, 24, 168, 720, 1440, 100000000]


    for (const [key, value] of Object.entries(data)) {
        var ind = 0;
        for (const ther of thers) {
            if (key<=ther) {
                yval[ind] = yval[ind] + value;
                break;
            }
            ind = ind+1;
        }
    }


    console.log(data);
    console.log(yval);

    var chartContainer = document.getElementById('two_two');
    var chart = echarts.init(chartContainer);

    var option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: { type: 'cross' }
        },
        xAxis: {
            type: 'category',
            data: ['1h', '3h', '1day', '1week', '1month', '2month', 'later'],
            name: "相隔时间",
            axisLabel: {
                formatter: '{value}'
            }
        },
        legend: {},
        yAxis: [{
            type: 'value',
            name: '问题数量',
            position: 'left',
            axisLabel: {
                formatter: '{value} 个'
            }
        }],
        series: [{
            name: '问题数量',
            data: yval,
            type: 'bar' // Customize chart type as per your needs
        }]
    };

    chart.setOption(option);
}