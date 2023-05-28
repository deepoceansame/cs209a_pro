function createChart41(data) {
    var labels = Object.keys(data);
    var values = Object.values(data);

    console.log(data);
    console.log(labels);
    console.log(values);

    var chartContainer = document.getElementById('four_one');
    var chart = echarts.init(chartContainer);

    var option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: { type: 'cross' }
        },
        xAxis: {
            type: 'category',
            data: labels,
            name: "用户个数",
            axisLabel: {
                formatter: '{value} 个'
            }
        },
        legend: {},
        yAxis: [{
            type: 'value',
            name: 'thread个数',
            position: 'left',
            axisLabel: {
                formatter: '{value} 个'
            }
        }],
        series: [{
            name: 'thread个数',
            data: values,
            type: 'bar' // Customize chart type as per your needs
        }]
    };

    chart.setOption(option);
}