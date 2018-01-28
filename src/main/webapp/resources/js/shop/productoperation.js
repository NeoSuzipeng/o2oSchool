$(function(){
	//获取请求参数中的商品Id
	var productId = getQueryString('productId');
	//获取商品信息的初始化URL    -----------未实现-----------
	var infoUrl = '/o2o/shopadmin/getproductbyId?productId=' + productId;
	//获取商品类别的初始化URL
	var productCategoryUrl = '/o2o/shopadmin/getproductcategorylist';
	//更新商品信息的URL -----------未实现-----------
	var productPostUrl = '/o2o/shopadmin/modifyproduct';
	var isEdit = false;
	if(productId){
		isEdit = true;
		getInfo(productId);
	}else{
		//获取商品类别供用户选择
		getCategory();
		productPostUrl = '/o2o/shopadmin/addproduct';
	}
	
	function getInfo(id){
		$.getJSON(infoUrl,function(data){
			// 从返回的JSON当中获取product对象的信息，并赋值给表单
			if(data.success){
				var product = data.product;
				$('#product-name').val(product.productName);
				$('#product-desc').val(product.productDesc);
				$('#point').val(product.point);
				$('#normal-price').val(product.normalPrice);
				$('#promotion-price').val(
						product.promotionPrice);
				// 获取原本的商品类别以及该店铺的所有商品类别列表
				var optionHtml = '';
				var optionArr = data.productCategoryList;
				var optionSelected = product.productCategory.productCategoryId;
				// 生成前端的HTML商品类别列表，并默认选择编辑前的商品类别
				optionArr
						.map(function(item, index) {
							var isSelect = optionSelected === item.productCategoryId ? 'selected'
									: '';
							optionHtml += '<option data-value="'
									+ item.productCategoryId
									+ '"'
									+ isSelect
									+ '>'
									+ item.productCategoryName
									+ '</option>';
						});
				$('#category').html(optionHtml);
			}
		});
	}
	
	function getCategory(){
		$.getJSON(productCategoryUrl,function(data){
			if(data.success){
				var productCategoryList = data.data;
				var tempHtml = '';
				productCategoryList.map(function(item,index){
					tempHtml += '<option data-value="'
						+ item.productCategoryId
						+'">'
						+item.productCategoryName
						+'</option>'
				});
			}
			$('#category').html(tempHtml);
		});
	}
	
	$(".detail-img-div").on("change", ".detail-img:last-child",function(){
		//控制上传图片组件
		//点击最后一个组件时生成一个新的组件，最大值为6
		if($(".detail-img").length < 6){
			$("#detail-img").append('<input type="file" class="detail-img">');
		}
	});
	
	$("#submit").click(function(){
		var product = {};
		//获取商品的基本信息
		product.productName = $('#product-name').val();
		product.productDesc = $('#product-desc').val();
		product.point = $('#point').val();
		product.promotionPrice = $('#promotion-price').val();
		product.normalPrice = $('#normal-price').val();
		product.productCategory = {
				productCategoryId : $('#category').find('option').not(
						function(){
							return !this.selected;
						}).data('value'),
				
		}
		//获取缩略图信息
		product.productId = productId;
		var thumbnail = $('#small-img')[0].files[0];
		var formData = new FormData();
		formData.append('thumbnail', thumbnail);
		//获取详情图信息
		$('.detail-img').map(function(index, item){
			if($('.detail-img')[index].files.length > 0){
				console.log($('.detail-img')[index].files[0]);
				formData.append('productImg'+index, $('.detail-img')[index].files[0]);
			}
		});
		//将商品
		formData.append('productStr', JSON.stringify(product));
		
		// 获取表单里输入的验证码
		var verifyCodeActual = $('#j_captcha').val();
		if (!verifyCodeActual) {
			$.toast('请输入验证码！');
			return;
		}
		formData.append("verifyCodeActual", verifyCodeActual);
		// 将数据提交至后台处理相关操作
		$.ajax({
			url : productPostUrl,
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.success) {
					$.toast('提交成功！');
					$('#captcha_img').click();
				} else {
					$.toast('提交失败！');
					$('#captcha_img').click();
				}
			}
		});
	});
})