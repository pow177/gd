window.onload = function(){

    //1.获取到列表的dom，刷新显示部分的dom，列表父容器的dom

    let container = document.querySelector('#refreshContainer');

    let refreshText = document.querySelector('.refreshText');

    let parent = document.querySelector('.main');



    //2.定义一些需要常用的变量

    let startY = 0;//手指触摸最开始的Y坐标

    let endY = 0;//手指结束触摸时的Y坐标



    //3.给列表dom监听touchstart事件,得到起始位置的Y坐标

    parent.addEventListener('touchstart',function(e){

        startY = e.touches[0].pageY;

    });

    //4.给列表dom监听touchmove事件，当移动到一定程度需要显示上面的文字

    parent.addEventListener('touchmove',function (e) {

        if(isTop() && (e.touches[0].pageY-startY) > 0){

            console.log('下拉了');

            refreshText.style.height = "50px";

            parent.style.transform = "translateY(50px)";

            parent.style.transition = "all ease 0.5s";

            refreshText.innerHTML = "释放立即刷新...";

        }

    });

    //5.给列表dom监听touchend事件，此时说明用户已经松开了手指，应该进行异步操作了

    parent.addEventListener('touchend',function (e) {

        if(isTop()){

            refreshText.innerHTML = "正在刷新...";

            setTimeout(function(){

                parent.style.transform = "translateY(0)";

                console.log('成功刷新');
                refreshText.innerHTML = "";
            },2000)

        }
            return;

    })

    function isTop(){

        var t = document.documentElement.scrollTop||document.body.scrollTop;

        return t === 0 ? true : false;

    }



}