$(function() {
	// 从地址栏中获取userAwardId
	var userAwardId = getQueryString('userAwardId');
	// 根据userAwardId获取用户奖品映射信息
	var awardUrl = '/o2o/frontend/getawardbyuserawardid?userAwardId='
			+ userAwardId;

	
			$.getJSON(
					awardUrl,
					function(data) {
						if (data.success) {
							// 获取奖品信息并显示
							var award = data.award;
							$('#award-img').attr('src', getContextPath() + award.awardImg);
							$('#create-time').text(
									new Date(data.userAwardMap.createTime)
											.Format("yyyy-MM-dd"));
							$('#award-name').text(award.awardName);
							$('#award-desc').text(award.awardDesc);
							var imgListHtml = '';
							// 若未去实体店兑换实体奖品，生成兑换礼品的二维码供商家扫描
							if (data.usedStatus == 0) {
								imgListHtml += '<div> <img src="/o2o/frontend/generateqrcode4award?userAwardId='
										+ userAwardId
										+ '" width="20%" height="20%"/></div>';
								$('#imgList').html(imgListHtml);
							}
						}
					});
	// 若点击"我的"，则显示侧栏
	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});
	$.init();
});
