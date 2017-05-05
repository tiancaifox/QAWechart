jQuery.extend(jQuery.validator.messages, {
    required: "必选字段",
	remote: "请修正该字段",
	email: "请输入正确格式的电子邮件",
	url: "请输入合法的网址",
	date: "请输入合法的日期",
	dateISO: "请输入合法的日期 (ISO).",
	number: "请输入合法的数字",
	digits: "只能输入整数",
	creditcard: "请输入合法的信用卡号",
	equalTo: "请再次输入相同的值",
	accept: "请输入拥有合法后缀名的字符串",
	maxlength: jQuery.validator.format("请输入一个 长度最多是 {0} 的字符串"),
	minlength: jQuery.validator.format("请输入一个 长度最少是 {0} 的字符串"),
	rangelength: jQuery.validator.format("请输入 一个长度介于 {0} 和 {1} 之间的字符串"),
	range: jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
	max: jQuery.validator.format("请输入一个最大为{0} 的值"),
	min: jQuery.validator.format("请输入一个最小为{0} 的值")
});
//匹配汉字  
jQuery.validator.addMethod("isChinese", function(value, element) {       
     return this.optional(element) || /^[\u4e00-\u9fa5]+$/.test(value);       
}, "只能输入中文字符");  
//判断英文字符 
jQuery.validator.addMethod("isEnglish", function(value, element) {       
     return this.optional(element) || /^[A-Za-z]+$/.test(value);       
}, "只能包含英文字符。");
//手机号码验证    
jQuery.validator.addMethod("isMobile", function(value, element) {    
  var length = value.length;    
  return this.optional(element) || (length == 11 && /^1[3|4|5|6|7|8]\d{9}$/.test(value));    
}, "请正确填写您的手机号码。");
//电话号码验证    
jQuery.validator.addMethod("isPhone", function(value, element) {    
  var tel = /^(\d{3,4}-?)?\d{7,9}$/g;    
  return this.optional(element) || (tel.test(value));    
}, "请正确填写您的电话号码。");

// 联系电话(手机/电话皆可)验证   
jQuery.validator.addMethod("isTel", function(value,element) {   
    var length = value.length;   
    var mobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;   
    var tel = /^(\d{3,4}-?)?\d{7,9}$/g;       
    return this.optional(element) || tel.test(value) || (length==11 && mobile.test(value));   
}, "请正确填写您的联系方式");
//匹配qq      
jQuery.validator.addMethod("isQq", function(value, element) {       
     return this.optional(element) || /^[1-9]\d{4,12}$/;       
}, "匹配QQ");   
//身份证号码验证
jQuery.validator.addMethod("isIdCardNo", function(value, element) { 
  //var idCard = /^(\d{6})()?(\d{4})(\d{2})(\d{2})(\d{3})(\w)$/;   
  return this.optional(element) || isIdCardNo(value);    
}, "请输入正确的身份证号码。"); 
//字符验证，只能包含中文、英文、数字、下划线等字符。    
jQuery.validator.addMethod("stringCheck", function(value, element) {       
     return this.optional(element) || /^[a-zA-Z0-9\u4e00-\u9fa5-_]+$/.test(value);       
}, "只能包含中文、英文、数字、下划线等字符"); 
//判断是否包含中英文特殊字符，除英文"-_"字符外
jQuery.validator.addMethod("isContainsSpecialChar", function(value, element) {  
     var reg = RegExp(/[(\ )(\`)(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\*)(\()(\))(\+)(\=)(\|)(\{)(\})(\')(\:)(\;)(\')(',)(\[)(\])(\.)(\<)(\>)(\/)(\?)(\~)(\！)(\@)(\#)(\￥)(\%)(\…)(\&)(\*)(\（)(\）)(\—)(\+)(\|)(\{)(\})(\【)(\】)(\‘)(\；)(\：)(\”)(\“)(\’)(\。)(\，)(\、)(\？)]+/);   
     return this.optional(element) || !reg.test(value);       
}, "含有中英文特殊字符"); 