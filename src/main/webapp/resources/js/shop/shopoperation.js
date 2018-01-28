$(function(){
	var shopId = getQueryString("shopId");
	var isEdit = shopId ? true : false;
	var initUrl = "/o2o/shopadmin/getshopinitinfo";
	var registerShopUrl = "/o2o/shopadmin/registershop";
	var getShopUrl = "/o2o/shopadmin/getshopbyid?shopId=" + shopId;
	var editShopUrl = "/o2o/shopadmin/modifyshop";
	if(isEdit){
		getShopInfo();
	}else{
		getShopInitInfo();
	}
	function getShopInfo(){
		$.getJSON(getShopUrl, function(data){
			if(data.success){
				var shop = data.shop;
				$('#shop-name').val(shop.shopName);
				$('#shop-addr').val(shop.shopAddr);
				$('#shop-phone').val(shop.Phone);
				$('#shop-desc').val(shop.shopDesc);
				var tempAreaHtml = '';
				var shopCategoryHtml = '<option data-id=' + shop.shopCategory.shopCategoryId 
			    + '>' + shop.shopCategory.shopCategoryName + '</option>';
				
				data.areaList.map(function(item, index){
						tempAreaHtml += '<option data-id=' + item.areaId 
					    + '>' + item.areaName + '</option>';
					});
				}
				$('#shop-category').html(shopCategoryHtml);
				$('#shop-category').attr('disabled', 'disabled');
				$('#area').html(tempAreaHtml);
				$("#area option[data-id='" + shop.area.areaId + "']").attr('selected', 'selected');	
		});
	}
	
	function getShopInitInfo(){
		$.getJSON(initUrl, function(data){
			if(data.success){
				var tempHtml = '';
				var tempAreaHtml = '';
				data.shopCategoryList.map(function(item, index){
					tempHtml += '<option data-id=' + item.shopCategoryId 
					    + '>' + item.shopCategoryName + '</option>';
				});
				
				data.areaList.map(function(item, index){
					tempAreaHtml += '<option data-id=' + item.areaId 
					    + '>' + item.areaName + '</option>';
				});
				
				$("#shop-category").html(tempHtml);
				$("#area").html(tempAreaHtml);
			}
		});
	}
	$('#submit').click(function(){
		var shop = {};
		if(isEdit){
			shop.shopId = shopId;
		}
		shop.shopName = $('#shop-name').val();
		shop.shopAddr = $('#shop-addr').val();
		shop.phone = $('#shop-phone').val();
		shop.shopDesc = $('#shop-desc').val();
		shop.shopCategory = {
				shopCategoryId : $('#shop-category').find('option').not(function(){
					return !this.selected;
				}).data('id')
		}
		
		shop.area = {
				areaId : $('#area').find('option').not(function(){
					return !this.selected;
				}).data('id')
		}
		
		var shopImg = $('#shop-img')[0].files[0]; 
		var shopData  = new FormData();
		shopData.append('shopImg', shopImg);
		shopData.append('shopStr', JSON.stringify(shop));
		var verifyCodeActual = $('#j_captcha').val();
		if(!verifyCodeActual){
			$.toast("输入验证码");
			return;
		}
		shopData.append('verifyCodeActual', verifyCodeActual);
		$.ajax({
			url:(isEdit ? editShopUrl : registerShopUrl),
			type:'POST',
			data:shopData,
			contentType:false,
			processData:false,
			cache:false,
			success:function(data){
				if(data.success){
					$.toast('提交成功！');
				}else{
					$.toast('提交失败，' + data.errorMsg);
				}
				$('#captcha_img').click();
			}
		});
	});
})