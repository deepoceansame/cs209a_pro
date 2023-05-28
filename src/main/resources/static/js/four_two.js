function createChart42 (data) {
    console.log(data);
    let wordcloud = echarts.init(document.getElementById("four_two"));
    let wordcloudData = [];

    console.log(typeof data, "42424242");

    for(const key in data) {
        console.log(key);
        wordcloudData.push({
            name: key,
            value: data[key]
        });
    }

    let wordcloudOption = {
        title: {
            text: 'Active User Id',
            textStyle: {
                fontStyle: 'oblique',
                fontSize: 20,
                color: '#4cc9f0'
            },
            left: 'center'
        },
        tooltip: {},
        series: [{
            type: 'wordCloud',
            shape: {
                cloudGrow: 0.2
            },
            sizeRange: [10, 60],
            rotationRange: [-30, 30],
            gridSize: 2,
            drawOutOfBound: false,
            layoutAnimation: true,
            keepAspect: true,
            textStyle: {
                fontWeight: 'bold',
                color: function () {
                    return 'rgb(' + [
                        Math.round(Math.random() * 160),
                        Math.round(Math.random() * 160),
                        Math.round(Math.random() * 160)
                    ].join(',') + ')';
                }
            },
            emphasis: {
                textStyle: {
                    shadowBlur: 15,
                    shadowColor: '#333'
                }
            },
            data: wordcloudData.sort(function (a, b) {
                return a.value - b.value;
            })
        }]
    };
    wordcloud.setOption(wordcloudOption);
}