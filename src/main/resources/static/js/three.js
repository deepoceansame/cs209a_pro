function createChart3 (data, domId) {

    let wordcloud = echarts.init(document.getElementById(domId));
    let wordcloudData = [];

    console.log(typeof data, "tttttt");

    for(const key in data) {
        console.log(key);
        wordcloudData.push({
            name: key,
            value: data[key]
        });
    }

    console.log(wordcloudData, domId);

    let wordcloudOption = {
        title: {
            text: 'Related Tag WordCloud',
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