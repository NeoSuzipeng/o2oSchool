$(function() {
	
	getProductSellDailyList();

	/**
	 * 获取7天的销量
	 */
	function getProductSellDailyList() {
		// 获取该店铺商品7天销量的URL
		var listProductSellDailyUrl = '/o2o/shopadmin/listproductselldailyinfobyshop';
		// 访问后台，该店铺商品7天销量的URL
		$.getJSON(listProductSellDailyUrl, function(data) {
			if (data.success) {
				var productSellDailyList = data.productSellDailyList;
				var tempHtml = '';
				// 遍历购买信息列表，拼接出列信息
				productSellDailyList.map(function(item, index) {
					tempHtml += '' + '<div class="row row-productbuycheck">'
							+ '<div class="col-40">' + item.product.productName
							+ '</div>'
							+ '<div class="col-40 productbuycheck-time">'
							+ item.total
							+ '</div>' ;
				});
				$('.productbuycheck-wrap').html(tempHtml);
			}
		});
	}
	

});