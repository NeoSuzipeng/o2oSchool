$(function(){
	// 获取此店铺下的商品列表的URL
	var listUrl = '/o2o/shopadmin/getproductlistbyshop?pageIndex=1&pageSize=999';
	// 商品下架URL
	var statusUrl = '/o2o/shopadmin/modifyproduct';
	
	getList();
	
	function getList(){
		$.getJSON(listUrl, function(data) {
			if (data.success) {
				var productList = data.productList;
				var tempHtml = '';
				// 遍历每条商品信息，拼接成一行显示，列信息包括：
				// 商品名称，优先级，上架\下架(含productId)，编辑按钮(含productId)
				// 预览(含productId)
				productList.map(function(item, index) {
					var textOp = "下架";
					var contraryStatus = 0;
					if (item.enableStatus == 0) {
						// 若状态值为0，表明是已下架的商品，操作变为上架(即点击上架按钮上架相关商品)
						textOp = "上架";
						contraryStatus = 1;
					} else {
						contraryStatus = 0;
					}
					// 拼接每件商品的行信息
					tempHtml += '' + '<div class="row row-product">'
							+ '<div class="col-33">'
							+ item.productName
							+ '</div>'
							+ '<div class="col-20">'
							+ item.point
							+ '</div>'
							+ '<div class="col-40">'
							+ '<a href="#" class="edit" data-id="'
							+ item.productId
							+ '" data-status="'
							+ item.enableStatus
							+ '">编辑</a>'
							+ '<a href="#" class="status" data-id="'
							+ item.productId
							+ '" data-status="'
							+ contraryStatus
							+ '">'
							+ textOp
							+ '</a>'
							+ '<a href="#" class="preview" data-id="'
							+ item.productId
							+ '" data-status="'
							+ item.enableStatus
							+ '">预览</a>'
							+ '</div>'
							+ '</div>';
				});
				// 将拼接好的信息赋值进html控件中
				$('.product-wrap').html(tempHtml);
			}
		});
	}
    
	$('.product-wrap').on('click', 'a', function(e){
		var target = $(e.currentTarget);
		if(target.hasClass('edit')){
			window.location.href = '/o2o/shopadmin/productoperation?productId='
				+ e.currentTarget.dataset.id;
		}
		if(target.hasClass('status')){
			changeItemStatus(e.currentTarget.dataset.id,
					e.currentTarget.dataset.status);
		}
		if(target.hasClass('preview')){
			//window.location.href = '/o2o/frontend/productdetail?productId='
				//+ e.currentTarget.dataset.id;
		}
	});
	
	function changeItemStatus(id, status){
		var product = {};
		product.productId = id;
		product.enableStatus = status;
		$.confirm("确定吗？",function(){
			$.confirm("确定吗？",
					function(){
				$.ajax({
					url:statusUrl,
					type:'POST',
					data:{
						productStr : JSON.stringify(product),
						statusChange : true
					},
					dataType: "application/json",
					success: function(data){
						if(data.success){
							$.toast("删除成功");
						}else{
							$.toast(data.errorMsg);
						}
					}
				})
			}
			);
		});
		
	}
})