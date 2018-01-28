$(function() {
	var loading = false;
	var maxItems = 999;
	var pageSize = 10;
	// 获取用户各店铺积分信息的URL
	var listUrl = '/o2o/frontend/listusershopmapsbycustomer';
	var pageNum = 1;
	var shopName = '';
	// 预先加载20条
	addItems(pageSize, pageNum);
	// 按照查询条件获取用户各店铺积分信息列表，并生成对应的HTML元素添加到页面中
	function addItems(pageSize, pageIndex) {
		// 生成新条目的HTML
		var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
				+ pageSize + '&shopName=' + shopName;
		loading = true;
		$.getJSON(url, function(data) {
			if (data.success) {
				// 获取总数
				maxItems = data.count;
				var html = '';
				data.userShopMapList.map(function(item, index) {
					html += '' + '<div class="card" data-shop-id="'
							+ item.shop.shopId + '">' + '<div class="card-header">'
							+ item.shop.shopName + '</div>'
							+ '<div class="card-content">'
							+ '<div class="list-block media-list">' + '<ul>'
							+ '<li class="item-content">'
							+ '<div class="item-inner">'
							+ '<div class="item-subtitle">本店积分:' + item.point
							+ '</div>' + '</div>' + '</li>' + '</ul>'
							+ '</div>' + '</div>' + '<div class="card-footer">'
							+ '<p class="color-gray">更新时间'
							+ new Date(item.createTime).Format("yyyy-MM-dd")
							+ '</p>' + '</div>' + '</div>';
				});
				$('.list-div').append(html);
				var total = $('.list-div .card').length;
				if (total >= maxItems) {
					// 加载完毕，则注销无限加载事件，以防不必要的加载
					$.detachInfiniteScroll($('.infinite-scroll'));
					// 删除加载提示符
					$('.infinite-scroll-preloader').remove();
					return;
				}
				pageNum += 1;
				loading = false;
				$.refreshScroller();
			}
		});
	}

	// 无极滚动，分页显示
	$(document).on('infinite', '.infinite-scroll-bottom', function() {
		if (loading)
			return;
		addItems(pageSize, pageNum);
	});

	// 绑定搜索事件，主要是按照店铺名模糊查询
	$('#search').on('change', function(e) {
		shopName = e.target.value;
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});
	// 若点击"我的"，则显示侧栏
	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});
	$.init();
});
