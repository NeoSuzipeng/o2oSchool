$(function(){
	getList();
	function getList(e){
		$.ajax({
			url:"/o2o/shopadmin/getshoplist",
			type:"get",
			dataType:"json",
			success:function(data){
				if(data.success){
					handlerList(data.shopList);
					handlerUser(data.user);
				}
			}
		});
	}
	
	function handlerUser(data){
		$("#user-name").text(data.name);
	}
	
	function handlerList(data){
		var shopListHtml = '';
		data.map(function(item, index){
			shopListHtml += '<div class="row row-shop"><div class="col-40">'
							+ item.shopName + '</div><div class="col-40">'
							+ shopStatus(item.enableStatus)
							+ '</div><div class="col-20">'
							+ goShop(item.enableStatus, item.shopId) + '</div></div>';
		});
		$(".shop-warp").html(shopListHtml);
	}
	
	function shopStatus(data){
		if(status == 0){
			return '审核中';
		}else if(status == '-1'){
			return '审核通过';
		}else{
			return '审核通过';
		}
	}
	
	function goShop(status, id){
		if(status == 1){
			return '<a href="/o2o/shopadmin/shopmanagement?shopId=' + id + '">进入</a>';
		}else{
			return '';
		}
	}
})