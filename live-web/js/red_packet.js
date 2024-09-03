function initRedPacket(num,redPacketConfigCode) {
    var dom = createDom(num);
    var wrapper = document.getElementById('wrapper');
    wrapper.appendChild(dom);
    console.log(num);
    console.log(redPacketConfigCode);
    bindEvent(redPacketConfigCode);
}

var totalMoney = 0;   //Total Money Grabbed
var delayTime = 0;
var lastImg = null;   //Last Dropped Image 

//Create DOM element for the red envelop, num means number of envelops
function createDom(num) {
    var frag = document.createDocumentFragment();  
    for (var i = 0; i < num; i++) {
        var img = new Image();
        img.src = '../img/petal.jpg';
        img.style.width = 140 +'px';
        img.style.left = ranNum(0, window.innerWidth) + 'px';      //Scattered Red Envelope
        var delay = ranNum(0, 100) / 10;
        img.style.animationDelay = delay + 's';                   //Setup red envelope delay time
        if (delayTime < delay) {
            delayTime = delay;
            lastImg = img;
        }
        //data-money
        img.dataset.money = ranNum(0, 1000) / 100;     //Set money inside each envelope
        frag.appendChild(img);
    }
    return frag;
}

//Bind clicking element
function bindEvent(redPacketConfigCode) {
    var wrapper = document.getElementById('wrapper'),
        imgList = document.getElementsByTagName('img'),
        modol = document.getElementById('modol'),
        text = document.getElementById('text'),
        btn = document.getElementById('btn');   
    
    //clicking to envelope
    addEvent(wrapper, 'mousedown', function (e) {
        var event = e || window.event,
            target = event.target || event.srcElement,
            money = event.target.dataset.money;
        //Get Envelope
        let data = new FormData();
        data.append("redPacketConfigCode",redPacketConfigCode);
        httpPost(getRedPacketUrl, data)
            .then(resp => {
                if (isSuccess(resp)) {
                    if(resp.data.price!=null) {
                        money = resp.data.price;
                        if (money) {
                            text.innerText = resp.data.msg;
                            for (var i = 0, len = imgList.length; i < len; i++) {
                                imgList[i].style.animationPlayState = 'paused';
                            }
                            modol.style.display = 'block';
                            totalMoney += Number(money);
                        }
                    } else {
                        text.innerText = resp.data.msg;
                        for (var i = 0, len = imgList.length; i < len; i++) {
                            imgList[i].style.animationPlayState = 'paused';
                        }
                        modol.style.display = 'block';
                    }
                }
        });
    });    
    //Click for continuing red envelope
    addEvent(btn, 'click', function () {
        modol.style.display = 'none';
        for (var i = 0, len = imgList.length; i < len; i++) {
            imgList[i].style.animationPlayState = 'running';
        }
    });
    //After all red envelope finished
    addEvent(lastImg, 'webkitAnimationEnd', function () {
        modol.style.display = 'block';
        text.innerText = 'Snatched ' + totalMoney.toFixed(2) + '¥';
        btn.style.display = 'none';
        setTimeout(() => modol.style.display='none', 3000);
    });

  
   
}

function ranNum(min, max) {
    return Math.ceil(Math.random() * (max - min) + min);
}

//Add event to eleemnt
function addEvent(elem, type, handle) {
    if (elem.addEventListener) {
        elem.addEventListener(type, handle, false);
    } else if (elem.attachEvent) {
        elem.attachEvent('on' + type, function () {
            handle.call(elem);
        })
    } else {
        elem['on' + type] = handle;
    }
}



