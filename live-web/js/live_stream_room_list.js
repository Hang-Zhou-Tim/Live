new Vue({
	el: '#app',
	data: {
		userId: 0,
		showLoginPop: false,
		loginCodeBtn: 'Get',
		lastTime: 60,
		mobile: '',
		code: '',
		hasSendSms: false,
		livingRoomList: {},
		isLogin: false,
		initInfo: {},
		loginBtnMsg: 'Login',
		showStartLivingBtn:true,
		listType: 1,
		page:1,
		pageSize:15,
		startLivingRoomTab: false,
		loadingNextPage: false,
		hasNextPage: true,
		currentChooseTab: null
	},

	//When the page is mounted, use these methods
	mounted() {
		this.initPage();
		//this.listLivingRoom(1);
		console.log('handler');
	},

	methods: {
		load:function() {
			console.log('this is load');
		},
		initPage:function() {
			var that = this;
			httpPost(homePageUrl, {}).then(resp => {
				console.log(resp.data);
				//Sucess
				if (resp.data.loginStatus == true) {
					that.initInfo=resp.data;
					that.loginBtnMsg='';
					that.isLogin = true;
					this.listLivingRoom(0);
				}
			})
		},

		chooseLivingType: function(type,id) {
			this.listLivingRoom(type);
			if(this.currentChooseTab!=null) {
				this.currentChooseTab.classList.remove('top-title-active');
			}
			this.currentChooseTab = document.getElementById(id);
			this.currentChooseTab.classList.add('top-title-active');
		},

		listLivingRoom: function(type) {
			var that = this;
			let data = new FormData();
			data.append("page",this.page);
			data.append("pageSize",this.pageSize);
			data.append("type",type);
			httpPost(listLivingRoomUrl,data).then(resp=>{
				console.log('Room List');
				//Login Successfully
				if(isSuccess(resp)) {
					that.livingRoomList = resp.data.list;
				}
			})
		},
		showLoginPopNow: function () {
			this.showLoginPop = true;
		},
		hiddenLoginPopNow: function () {
			this.showLoginPop = false;
		},

		mobileLogin: function () {
			if (this.code == '') {
				this.$message.error('Please Enter Validation Code!');
				return;
			}
			var checkStatus = this.checkPhone();
			if(!checkStatus) {
				return;
			}
			var that = this;
			let data = new FormData();
			data.append("phone",this.mobile);
			data.append("code",this.code);
			httpPost(loginUrl,data).then(resp=>{
				//Login Successfully
				if (resp.code == 200) {
					window.alert("login success!");
					that.userId=resp.data.userId;
					that.$message.success('Login Successfully!');
					that.hiddenLoginPopNow();
					that.isLogin=true;
					that.userId=resp.data.userId;
					that.initPage();
				} else {
					window.alert("login failed!");
					console.log(resp.data);
					that.$message.error(resp.msg);
				}
			})
		},

		showStartLivingRoomTab: function() {
            this.startLivingRoomTab = true;
        },
		startLivingRoom: function () {
			this.toLivingRoom();
        },

		jumpToLivingRoomPage(livingType) {
			console.log(this.isLogin);
			if(!this.isLogin) {
				this.$message.error('Please Login! ');
				return;
			}
			let data = new FormData();
			data.append("type",livingType);
			//Request to Start Live Stream
			httpPost(startLiving,data).then(resp=>{
				//Start Live Stream
				if(isSuccess(resp)) {
					if(livingType == 1) {
						//Go to live page
						window.location.href = "./live_stream_room.html?roomId=" + resp.data.roomId;
					} else if (livingType==2) {
						window.location.href = "./live_stream_room_pk.html?roomId=" + resp.data.roomId;
					}
				    
				} else {
					that.$message.error(resp.msg);
				}
			})

        },
		jumpToLivingRoom(roomId,type) {
			if(!this.isLogin) {
				this.$message.error('Please Login First!');
				return;
			}
			if(type==1) {
				window.location.href = "./live_stream_room.html?roomId=" + roomId; //go to the live stream room
			} else if(type==2) {
				window.location.href = "./live_stream_room_pk.html?roomId=" + roomId; //go to the pk stream room
			}
		},

		sendSmsCode: function () {
			if (this.hasSendSms) {
				return;
			}
			console.log(this.mobile);
			var checkStatus = this.checkPhone();
			if(!checkStatus) {
				return;
			}
			//Send Validation Code
			var that = this;
			let data = new FormData();
			window.alert("code sent!");
			data.append("phone",this.mobile);
			//SMS Request Interface.
			httpPost(sendSmsUrl,data).then(resp=>{
				if(resp.code==200)	{
					that.hasSendSms = true;
					that.$message.success('Finish Sending');
					that.code = '1000';
					var interval = setInterval(function () {
						that.loginCodeBtn = 'Wait(' + that.lastTime + 's)';
						if (that.lastTime == 0) {
							that.lastTime = 60;
							that.loginCodeBtn = 'Get';
							that.hasSendSms = false;
							console.log('Clear Timer');
							clearInterval(interval);
							return;
						} else {
							that.lastTime = that.lastTime - 1;
						}
					}, 1000);
				} else {
					that.$message.error(resp.msg);
				}
			})
		},

		checkPhone: function(){
			let phoneReg = /(^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$)/;
			if (this.mobile == '' || !phoneReg.test(this.mobile)) {
				this.$message.error('Wrong Phone Format');
				return false;
			}
			return true;
		},

		initLoad: function() {
			   	let that = this;
				window.addEventListener('scroll', function() {
					let scrollTop=document.documentElement.scrollTop	  //The height that the user already scrolls. Will Refresh when user activates the listener.
					let scrollHeight=document.documentElement.scrollHeight//The height that the scroll line represents. 
					let clientHeight=document.documentElement.clientHeight//The height of client windows
					//If scrollHeight is 1000, the user has already scrolled 800, the height of client windows is 100, distant to prepare refreshing is 100, then requests new page.
					if(scrollTop+clientHeight>=scrollHeight-100 && that.loadingNextPage==false && that.hasNextPage == true){
					  that.loadingNextPage = true;
					  console.log('Schroll to the ends');
					  
					  that.page = that.page + 1;
					  let data = new FormData();
					  data.append("page",that.page);
					  data.append("pageSize",that.pageSize);
					  data.append("type",that.listType);
					  httpPost(listLivingRoomUrl,data).then(resp=>{
							//Successfully finished
							if(isSuccess(resp)) {
								let livingRoomTempList = resp.data.list;
								for (i = 0; i < livingRoomTempList.length; i++) {
									that.livingRoomList.push(livingRoomTempList[i]);
								}
								if(!resp.data.hasNext) {
									that.hasNextPage = false;
								} 
								that.loadingNextPage = false;

							}
						})
					}
				});
		}
	}

})