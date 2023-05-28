function createChart13(data) {



    var labels = Object.keys(data);
    var values = Object.values(data);

    console.log(data);
    console.log(labels);
    console.log(values);

    var chartContainer = document.getElementById('one_three');
    var chart = echarts.init(chartContainer);

    var option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: { type: 'cross' }
        },
        xAxis: {
            type: 'category',
            data: labels,
            name: "答案数量",
            axisLabel: {
                formatter: '{value} 个'
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
            data: values,
            type: 'bar' // Customize chart type as per your needs
        }]
    };

    chart.setOption(option);
}

// document.addEventListener("DOMContentLoaded", function() {
//     var data = [[${solNumAnswer.ansCntDisMap}]];
//     createChart(data);
// });