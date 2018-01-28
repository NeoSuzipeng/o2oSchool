$(function(){
	var shopId = getQueryString('shopId');
	var shopInfoUrl = '/o2o/shopadmin/getmanagementinfo?shopId=' + shopId;
	$.getJSON(shopInfoUrl, function(data){
		if(data.redirect){
			window.location.href = data.url;
		}else{
			if(data.shopId != undefined && data.shopId != null){
				shopId = data.shopId;
			}
			$("#shop-info").attr('href', '/o2o/shopadmin/shopoperation?shopId=' + shopId);
		}
	});
})