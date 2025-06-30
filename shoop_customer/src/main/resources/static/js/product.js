// 获取URL中的商品ID
function getProductIdFromUrl() {
    var path = decodeURI(window.location.href);
    var arr1 = path.split("?");
    if (arr1.length < 2) return null;
    var arr2 = arr1[1].split("=");
    return arr2[1];
}

// 加载商品详情
function loadProductDetail(pid) {
    if (!pid) return;
    
    // 先检查是否已收藏
    checkFavoriteStatus(pid);
    
    $.ajax({
        url: '/product/getProDetail?pid=' + pid,
        type: 'get',
        dataType: 'json',
        success: function(resp) {
            if (resp.code == 200) {
                $("#product-title").text(resp.data.title);
                $("#product-price").text(resp.data.price);
                $("#n").text(resp.data.num);
                
                // 保存商品ID到按钮的data属性
                $("#btn-add-to-cart").data('pid', pid);
                $("#btn-add-to-collect").data('pid', pid);
                
                // 加载图片
                for (let i = 1; i <= 5; i++) {
                    $("#product-image-" + i + "-big").attr("src", resp.data.image + i + "_big.png");
                    $("#product-image-" + i).attr("src", resp.data.image + i + ".jpg");
                }
            } else {
                alert("商品信息不存在");
            }
        },
        error: function() {
            alert("加载商品信息失败，请稍后重试");
        }
    });
}

// 检查商品是否已收藏
function checkFavoriteStatus(pid) {
    $.ajax({
        url: '/favoretise/check',
        type: 'GET',
        data: { pid: pid },
        success: function(resp) {
            if (resp.code === 200) {
                // 更新收藏按钮状态
                if (resp.data === true) {
                    updateFavoriteButton(true);
                } else {
                    updateFavoriteButton(false);
                }
            }
        },
        error: function(xhr) {
            // 如果未登录，不更新按钮状态
            if (xhr.status !== 401 && xhr.status !== 403) {
                console.error('检查收藏状态失败:', xhr);
            }
        }
    });
}

// 更新收藏按钮状态
function updateFavoriteButton(isFavorite) {
    const $btn = $("#btn-add-to-collect");
    if (isFavorite) {
        $btn.html('<span class="fa fa-heart"></span> 已收藏')
            .removeClass('btn-default')
            .addClass('btn-danger')
            .prop('disabled', true);
    } else {
        $btn.html('<span class="fa fa-heart-o"></span> 加入收藏')
            .removeClass('btn-danger')
            .addClass('btn-default')
            .prop('disabled', false);
    }
}

// 加入购物车
function addToCart(pid, num, price) {
    // 直接调用加入购物车接口，由后端检查登录状态
    $.ajax({
        url: '/cart/addProToCart',
        type: 'POST',
        data: {
            num: num,
            pid: pid,
            price: price
        },
        success: function(resp) {
            if (resp.code == 200) {
                alert('商品已成功加入购物车！');
            } else if (resp.code == 300) {
                // 未登录，跳转到登录页
                window.location.href = '/login.html?returnUrl=' + encodeURIComponent(window.location.pathname);
            } else {
                alert('加入购物车失败：' + (resp.msg || '未知错误'));
            }
        },
        error: function(xhr) {
            if (xhr.status === 403 || xhr.status === 401) {
                // 未授权或禁止访问，跳转到登录页
                window.location.href = '/login.html?returnUrl=' + encodeURIComponent(window.location.pathname);
            } else {
                alert('加入购物车失败，请稍后重试');
                console.error('加入购物车失败:', xhr);
            }
        }
    });
}

$(function() {
    // 加载商品详情
    var pid = getProductIdFromUrl();
    if (pid) {
        loadProductDetail(pid);
    }
    
    /*商品小图片加鼠标移入加边框、移出移除边框*/
    $(".img-small").hover(function() {
            $(this).css("border", "1px solid #4288c3");
        },
        function() {
            $(this).css("border", "");
        })
	//点击时变化大图片
	$(".img-small").click(function() {
			//获得点击的小图片数据
			var n = $(this).attr("data");
			//所有大图隐藏
			$(".img-big").hide();
			//显示点击的小图对应的大图
			$(".img-big[data='" + n + "']").show();
		})
		//购物数量加1
	$("#numUp").click(function() {
		var n = parseInt($("#num").val());
		$("#num").val(n + 1);
	})
	//购物数量-1
	$("#numDown").click(function() {
		var n = parseInt($("#num").val());
		if (n == 1) {
			return;
		}
		$("#num").val(n - 1);
	})
    // 加入购物车按钮点击事件
    $("#btn-add-to-cart").click(function() {
        const pid = $(this).data('pid');
        if (!pid) {
            alert('商品信息加载中，请稍后再试');
            return;
        }
        const num = parseInt($("#num").val());
        const price = parseFloat($("#product-price").text()) * 100; // 转换为分
        addToCart(pid, num, price);
    });

    // 加入收藏按钮点击事件
    $("#btn-add-to-collect").click(function() {
        const pid = $(this).data('pid');
        if (!pid) {
            alert('商品信息加载中，请稍后再试');
            return;
        }
        addToFavorite(pid);
    });

    $(".img-big:eq(0)").show();
});

// 添加商品到收藏
function addToFavorite(pid) {
    $.ajax({
        url: '/favoretise/addFavoretise',
        type: 'POST',
        data: {
            pid: pid
        },
        success: function(resp) {
            if (resp.code == 200) {
                alert('商品已成功添加到收藏夹！');
                // 更新按钮状态或UI
                $('#btn-add-to-collect').html('<span class="fa fa-heart"></span> 已收藏')
                    .removeClass('btn-default')
                    .addClass('btn-danger')
                    .prop('disabled', true);
            } else if (resp.code == 300) {
                // 未登录，跳转到登录页
                window.location.href = '/login.html?returnUrl=' + encodeURIComponent(window.location.pathname);
            } else {
                alert('添加到收藏夹失败：' + (resp.msg || '未知错误'));
            }
        },
        error: function(xhr) {
            if (xhr.status === 403 || xhr.status === 401) {
                // 未授权或禁止访问，跳转到登录页
                window.location.href = '/login.html?returnUrl=' + encodeURIComponent(window.location.pathname);
            } else {
                alert('添加到收藏夹失败，请稍后重试');
                console.error('添加到收藏夹失败:', xhr);
            }
        }
    });
}