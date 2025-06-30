// 页面加载完成后执行
$(document).ready(function() {
    console.log('页面加载完成，开始初始化...');
    // 检查用户是否登录
    checkLoginStatus();

    // 商品列表悬停效果
    $(document).on('mouseenter', '.goods-panel', function() {
        $(this).css('box-shadow', '0 2px 10px rgba(0,0,0,0.1)');
    }).on('mouseleave', '.goods-panel', function() {
        $(this).css('box-shadow', 'none');
    });
});

// 检查登录状态
function checkLoginStatus() {
    $.ajax({
        url: '/users/showOneUser',
        type: 'GET',
        dataType: 'json',
        success: function(response) {
            // 检查响应是否有效
            if (response && response.uid) {
                console.log('用户已登录，用户ID:', response.uid);
                // 用户已登录，加载收藏列表
                loadFavorites();
            } else {
                console.log('用户未登录或会话已过期，跳转到登录页');
                window.location.href = '/login.html?returnUrl=' + encodeURIComponent(window.location.pathname);
            }
        },
        error: function(xhr) {
            console.error('获取用户信息失败:', xhr.status, xhr.responseText);
            if (xhr.status === 401 || xhr.status === 0) {
                // 401 未授权 或 0 可能是跨域或网络问题
                window.location.href = '/login.html?returnUrl=' + encodeURIComponent(window.location.pathname);
            } else {
                $('#favorites-container').html(`
                    <div class="col-md-12 text-center" style="padding: 50px 0;">
                        <div class="alert alert-danger">
                            <i class="fa fa-exclamation-circle"></i> 获取用户信息失败，请<a href="/login.html?returnUrl=${encodeURIComponent(window.location.pathname)}" class="alert-link">重新登录</a> 或 
                            <a href="javascript:location.reload()" class="alert-link">点击刷新</a> 重试
                        </div>
                    </div>`);
            }
        }
    });
}

// 加载收藏列表
function loadFavorites() {
    console.log('开始加载收藏列表...');
    const $container = $('#favorites-container');
    
    // 显示加载中状态
    $container.html(`
        <div class="col-md-12 text-center" style="padding: 50px 0;">
            <div class="spinner-border text-primary" role="status">
                <span class="sr-only">加载中...</span>
            </div>
            <p class="mt-2">正在加载收藏列表...</p>
        </div>`);
    
    // 添加请求超时设置
    $.ajax({
        url: '/favoretise/list',
        type: 'GET',
        dataType: 'json',
        timeout: 10000, // 10秒超时
        success: function(res) {
            console.log('收藏列表接口返回:', res);
            if (res && res.code === 200) {
                if (Array.isArray(res.data) && res.data.length > 0) {
                    renderFavorites(res.data);
                } else {
                    console.log('收藏列表为空');
                    showEmpty();
                }
            } else {
                const errorMsg = res && res.msg ? res.msg : '服务器返回数据格式错误';
                console.error('获取收藏列表失败:', errorMsg);
                showError('获取收藏列表失败：' + errorMsg);
            }
        },
        error: function(xhr, status, error) {
            console.error('请求收藏列表失败:', status, error);
            console.log('响应内容:', xhr.responseText);
            
            let errorMsg = '网络异常，请稍后重试';
            
            if (xhr.status === 0) {
                errorMsg = '无法连接到服务器，请检查网络连接';
            } else if (xhr.status === 401) {
                errorMsg = '登录已过期，请<a href="/login.html?returnUrl=' + encodeURIComponent(window.location.pathname) + '">重新登录</a>';
            } else if (xhr.status === 404) {
                errorMsg = '请求的接口不存在';
            } else if (xhr.status >= 500) {
                errorMsg = '服务器内部错误，请稍后再试';
            } else if (status === 'timeout') {
                errorMsg = '请求超时，请检查网络后重试';
            }
            
            showError(errorMsg);
        }
    });
}

// 渲染收藏列表
function renderFavorites(favorites) {
    console.log('开始渲染收藏列表，数据量:', favorites.length);
    const $container = $('#favorites-container');
    $container.empty();
    
    if (!Array.isArray(favorites) || favorites.length === 0) {
        console.log('收藏列表数据为空');
        showEmpty();
        return;
    }

    try {
        const $row = $('<div class="row"></div>');
        $container.append($row);
        
        favorites.forEach((fav, index) => {
            console.log(`处理第${index + 1}个收藏项:`, fav);
            // 确保图片URL有效
            const imageUrl = fav.image ? fav.image : '/images/default-product.png';
            // 价格处理，确保是数字
            const price = fav.price ? (fav.price / 100).toFixed(2) : '0.00';
            
            const html = `
                <div class="col-md-3 col-sm-6 mb-4">
                    <div class="card h-100">
                        <a href="/web/product.html?pid=${fav.pid || ''}" class="text-center p-3" style="height: 200px; display: flex; align-items: center; justify-content: center;">
                            <img src="${imageUrl}" class="card-img-top img-fluid" alt="${fav.title || '商品图片'}" 
                                 style="max-height: 100%; max-width: 100%; object-fit: contain;"
                                 onerror="this.src='/images/default-product.png'">
                        </a>
                        <div class="card-body d-flex flex-column">
                            <h5 class="card-title" style="height: 40px; overflow: hidden; margin-bottom: 10px;">
                                <a href="/web/product.html?pid=${fav.pid || ''}" class="text-dark" style="text-decoration: none;">
                                    ${fav.title || '未命名商品'}
                                </a>
                            </h5>
                            <p class="card-text text-danger font-weight-bold mb-3">¥${price}</p>
                            <div class="mt-auto">
                                <button class="btn btn-outline-danger btn-sm btn-block cancel-fav" 
                                        data-pid="${fav.pid || ''}">
                                    <i class="fa fa-trash-o"></i> 取消收藏
                                </button>
                            </div>
                        </div>
                    </div>
                </div>`;
            $row.append(html);
        });
        console.log('收藏列表渲染完成');
    } catch (error) {
        console.error('渲染收藏列表时出错:', error);
        showError('加载收藏列表时出错，请刷新页面重试');
    }
}

// 显示错误信息
// 显示错误信息
function showError(message) {
    const $container = $('#favorites-container');
    $container.html(`
        <div class="col-md-12 text-center" style="padding: 50px 0;">
            <div class="alert alert-danger" style="max-width: 600px; margin: 0 auto 20px; text-align: left; padding: 20px;">
                <h4 style="margin-top: 0;"><i class="fa fa-exclamation-triangle"></i> 出错了</h4>
                <p style="margin: 15px 0;">${message}</p>
                <div style="margin-top: 20px; text-align: center;">
                    <button class="btn btn-primary me-2" onclick="location.reload()" style="margin: 0 5px;">
                        <i class="fa fa-refresh"></i> 刷新页面
                    </button>
                    <a href="/" class="btn btn-outline-secondary" style="margin: 0 5px;">
                        <i class="fa fa-home"></i> 返回首页
                    </a>
                </div>
            </div>
            <p class="text-muted small">如果问题持续存在，请联系客服或稍后再试</p>
        </div>`);
}

// 显示空状态
function showEmpty() {
    const $container = $('#favorites-container');
    $container.html(`
        <div class="col-md-12 text-center" style="padding: 50px 0;">
            <i class="fa fa-heart-o" style="font-size: 48px; color: #ddd;"></i>
            <p class="mt-3 text-muted">您还没有收藏任何商品</p>
            <a href="/web/index.html" class="btn btn-primary mt-3">
                <i class="fa fa-shopping-bag"></i> 去逛逛
            </a>
        </div>`);
}

// 取消收藏
$(document).on('click', '.cancel-fav', function() {
    const pid = $(this).data('pid');
    const $btn = $(this);
    
    if (!confirm('确定要取消收藏该商品吗？')) {
        return;
    }
    
    $btn.prop('disabled', true).html('<i class="fa fa-spinner fa-spin"></i> 处理中...');
    
    $.ajax({
        url: '/favoretise/dropByUidAndPid',
        type: 'POST',
        data: { pid: pid },
        success: function(res) {
            if (res.code === 200) {
                // 移除对应的商品卡片
                $btn.closest('.col-md-3').fadeOut(300, function() {
                    $(this).remove();
                    // 检查是否还有收藏商品
                    if ($('#favorites-container .col-md-3').length === 0) {
                        showEmpty();
                    }
                });
            } else {
                $btn.prop('disabled', false).html('<i class="fa fa-trash-o"></i> 取消收藏');
                alert('取消收藏失败：' + (res.msg || '未知错误'));
            }
        },
        error: function(xhr) {
            console.error('取消收藏失败:', xhr);
            $btn.prop('disabled', false).html('<i class="fa fa-trash-o"></i> 取消收藏');
            alert('取消收藏失败，请稍后重试');
        }
    });
});
