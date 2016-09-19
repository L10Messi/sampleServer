/**
 * 系统的消息弹出方法
 * */
function showSuccess(){
	$.messager.show({
		title:'消息',
		timeout:2000,
		msg:"<div style='margin: 12px 0px 0px 72px;'><img src='../images/ok.png'></div><div style='margin: -14px 0px 0px 92px;'><span style='color:#006600;'>操作成功</span></div>",
		showType:'show'
	});
}
function showError(){
	$.messager.show({
		title:'消息',
		timeout:2000,
		msg:"<div style='margin: 12px 0px 0px 72px;'><img src='../images/no.png'></div><div style='margin: -14px 0px 0px 92px;'><span style='color:#FF0000;'>操作失败</span></div>",
		showType:'show'
	});
}
/**
 * 添加tab方法
 */
function addTab(options){
	var tabDefaultsOptions = {
		jqObj : $(''),
		title : '',
		url : '',
		closable : true
	};

	var opts = $.extend({}, tabDefaultsOptions, options);

	if (opts.jqObj.tabs('exists', opts.title)) {
		opts.jqObj.tabs('close', opts.title);
	}
	opts.jqObj.tabs('add', {
		title : opts.title,
		content : '<iframe scrolling="auto" frameborder="0" src="' + opts.url + '" style="width:100%;height:100%;"></iframe>',
		closable : opts.closable
	});

};
/**
 * 搜索框的重置方法
 * */
function reset(id){
	$('"#'+id+'"').form('clear');
}



