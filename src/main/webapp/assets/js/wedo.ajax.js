/**
 * author bmf
 * wedo ajax相关工具
 */
(function($) {
	if (!_webRoot && _webRoot == undefined) {
		throw new SyntaxError('the _webRoot is not defined!.'); 
	}
	var escape = /["\\\x00-\x1f\x7f-\x9f]/g,
	meta = {
		'\b': '\\b',
		'\t': '\\t',
		'\n': '\\n',
		'\f': '\\f',
		'\r': '\\r',
		'"': '\\"',
		'\\': '\\\\'
	},
	hasOwn = Object.prototype.hasOwnProperty;
	$.fn.attr2 = function() {
	    var val = this.prop.apply(this, arguments);
	    if ( ( val && val.jquery ) || typeof val === 'string' ) {
	        return val;
	    }
	    return this.attr.apply(this, arguments);
	};
	$.quoteString = function (str) {
		if (str.match(escape)) {
			return '"' + str.replace(escape, function (a) {
				var c = meta[a];
				if (typeof c === 'string') {
					return c;
				}
				c = a.charCodeAt();
				return '\\u00' + Math.floor(c / 16).toString(16) + (c % 16).toString(16);
			}) + '"';
		}
		return '"' + str + '"';
	};
	$.isExists = function (obj) {
		return obj != undefined && obj;
	}
	
	/**
	 * 
	 * 字符串转js对象
	 * @param str {String}
	 */
	$.secureEvalJSON  = function (src) {
	   if (typeof(JSON) == 'object' && JSON.parse) {
		   return JSON.parse(src);
	   }
	   var filtered = src;
       filtered = filtered.replace(/\\["\\\/bfnrtu]/g, '@');
       filtered = filtered.replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']');
       filtered = filtered.replace(/(?:^|:|,)(?:\s*\[)+/g, '');
       
       if (/^[\],:{}\s]*$/.test(filtered))
           return eval("(" + src + ")");
       else
           throw new SyntaxError("Error parsing JSON, source is not valid.");
	};

	/**
	 * object转字符串 
	 * @param o {Mixed} 
	 */
	$.toJSON = function (o) {
		if (o === null) {
			return 'null';
		}
		var pairs, k, name, val,
			type = $.type(o);

		if (type === 'undefined') {
			return undefined;
		}
		if (type === 'number' || type === 'boolean') {
			return String(o);
		}
		if (type === 'string') {
			return $.quoteString(o);
		}
		if (typeof o.toJSON === 'function') {
			return $.toJSON(o.toJSON());
		}
		if (type === 'date') {
			var month = o.getUTCMonth() + 1,
				day = o.getUTCDate(),
				year = o.getUTCFullYear(),
				hours = o.getUTCHours(),
				minutes = o.getUTCMinutes(),
				seconds = o.getUTCSeconds(),
				milli = o.getUTCMilliseconds();

			if (month < 10) {
				month = '0' + month;
			}
			if (day < 10) {
				day = '0' + day;
			}
			if (hours < 10) {
				hours = '0' + hours;
			}
			if (minutes < 10) {
				minutes = '0' + minutes;
			}
			if (seconds < 10) {
				seconds = '0' + seconds;
			}
			if (milli < 100) {
				milli = '0' + milli;
			}
			if (milli < 10) {
				milli = '0' + milli;
			}
			return '"' + year + '-' + month + '-' + day + 'T' +
				hours + ':' + minutes + ':' + seconds +
				'.' + milli + 'Z"';
		}

		pairs = [];

		if ($.isArray(o)) {
			for (k = 0; k < o.length; k++) {
				pairs.push($.toJSON(o[k]) || 'null');
			}
			return '[' + pairs.join(',') + ']';
		}
		if (typeof o === 'object') {
			for (k in o) {
				if (hasOwn.call(o, k)) {
					type = typeof k;
					if (type === 'number') {
						name = '"' + k + '"';
					} else if (type === 'string') {
						name = $.quoteString(k);
					} else {
						continue;
					}
					type = typeof o[k];

					if (type !== 'function' && type !== 'undefined') {
						val = $.toJSON(o[k]);
						pairs.push(name + ':' + val);
					}
				}
			}
			return '{' + pairs.join(',') + '}';
		}
	};
	
	$.wedoAjax = function(options) {
		return $.ajax({
			url:  options.url,
		    type: options.type || 'post',
		    data : options.data,
		    contentType : 'application/json',
		    dataType : 'json',
		    async : options.async === undefined ? true :  options.async,
			error : function(textStatus, errorThrown){
			        var returnMeg = {};
			        try {
			        		returnMeg = $.secureEvalJSON(textStatus.responseText);
			        		if (returnMeg.status == 'success') {
			        			this.success(returnMeg);
			        			return;
			        		}
			        	} catch (e) {
			        		throw e;
			        	}
			        	this.throwErr(returnMeg);
			   },
			   success : function(rpd) {
			        	// 调用成功，返回错误消息的情况
			        	var returnMeg = {};
			        	if (!rpd.status) {
			        		try {
			        				returnMeg = $.secureEvalJSON(rpd);
			        			} catch (e) {
					        		console.error(e);
					        		returnMeg.status = 'error';
					        	}
			        	} else {
			        			returnMeg = rpd;
			        	}
		        		if (returnMeg.status == 'error' || returnMeg.status == '-1') {
		        		   this.throwErr(returnMeg);
		        		}
			        	// 没有sucess函数默认显示的
			        	if (options.success && typeof options.success == 'function') {
			        		options.success(returnMeg);
			        	} else {
			        		bootbox.alert('请求成功!');
			        	}
			      },
			      throwErr: function(returnMeg) {
			          var msg = returnMeg.message || returnMeg.info || returnMeg.data.message || "";
                      bootbox.alert('出错了\t\n' + msg );
                      if (options.afterError && typeof options.afterError == 'function') {
                          options.afterError(returnMeg);
                      }
			      }
		});
	}
	
	/**
	 * 上传控件
	 */
	$.fn.uploadFileInput = function(option) {
        var input = $(this);
        $.getFileUploadUrl(function(url) {
            startDropzone(url, input.attr("id"), option);
        });
    };
	
	function startDropzone(responseUrl, id, option) {
		var dropzone = new Dropzone('#'+id+'', {
			url : responseUrl,
			method : "post",
			paramName: "file",
			dictRemoveFile: "删除文件",
			dictInvalidFileType: "不支持的附件类型",
			acceptedFiles: $.isExists(option) && $.isExists(option.acceptedFiles) ? option.acceptedFiles : "image/*",
			addRemoveLinks: true,
		    previewTemplate: document.querySelector('#preview-template').innerHTML,
		    parallelUploads: 1,
		    uploadMultiple: false,
		    maxFilesize: 1,
		    maxFiles: 1,
		    filesizeBase: 1000,
		    thumbnailWidth: $.isExists(option) && $.isExists(option.width) ? option.width : 300,
		    thumbnailHeight: $.isExists(option) && $.isExists(option.width) ? option.width : 170,
		    thumbnail: function(file, dataUrl) {
		      if (file.previewElement) {
		        file.previewElement.classList.remove("dz-file-preview");
		        var images = file.previewElement.querySelectorAll("[data-dz-thumbnail]");
		        for (var i = 0; i < images.length; i++) {
		          var thumbnailElement = images[i];
		          thumbnailElement.alt = file.name;
		          thumbnailElement.src = dataUrl;
		        }
		        setTimeout(function() { file.previewElement.classList.add("dz-image-preview"); }, 1);
		      }
		    },
		    //新增加方法，这里不适用removedfile事件的原因在于removedfile中模板html已经脱离dom,无法获取父节点
		    beforeRemovedfile: function(file){
		    	_ref1 = file.previewElement.parentElement.querySelectorAll("[fileid]")
		          for (_j = 0, _len1 = _ref1.length; _j < _len1; _j++) {
		            node = _ref1[_j];
		            node.value = "";
		            $(node).trigger("blur");
		          }
		    }
		  });
		
	  Dropzone.prototype.filesize = function(size) {
		    var units = [ 'TB', 'GB', 'MB', 'KB', 'b' ],
		        selectedSize, selectedUnit;

		    for (var i = 0; i < units.length; i++) {
		      var unit = units[i],
		          cutoff = Math.pow(this.options.filesizeBase, 4 - i) / 10;

		      if (size >= cutoff) {
		        selectedSize = size / Math.pow(this.options.filesizeBase, 4 - i);
		        selectedUnit = unit;
		        break;
		      }
		    }

		    selectedSize = Math.round(10 * selectedSize) / 10;

		    return '<strong>' + selectedSize + '</strong> ' + selectedUnit;

		  }
	  dropzone.on('complete', function(file) {
		    file.previewElement.classList.add('dz-complete');
		    
		  });
	  dropzone.on('success', function(file) {
		  var response = $.secureEvalJSON(file.xhr.response);
		  if (response.status == "success") {
			  _ref1 = file.previewElement.parentElement.querySelectorAll("[fileid]")
	          for (_j = 0, _len1 = _ref1.length; _j < _len1; _j++) {
	            node = _ref1[_j];
	            node.value = response.data.fid;
	            $(node).trigger("blur");
	          }
		  } 
		  dropzone.disable();
	  });
	  dropzone.on('removedfile', function(file) {
		  dropzone.enable();
	  });
	  dropzone.on("addedfile", function(file) {
		  var  others = file.previewElement.parentNode.childNodes;
			var i = 0;
			for (; i < others.length; i++) {
				  if (others[i].nodeType==1 && others[i].id == "show") {
					  others[i].style.display = "none";
				  }
			  }
	 });
	  
	}
	/**
 	 * ajax方式提交表单
 	 * 参数options.exclude：包含该关键字的input name将过滤掉
  	 * options.beforeSubmit{function} 提交前data处理，
	 * options.textOnly 如果该选项为true,只选择input[text]类型,默认false
	 * 含有onlyShow 属性的不序列化
	 * options.afterError 出错后的清除函数
	 * @param options {object}
	 */
$.fn.ajaxSubmit = function(options) {
 
    var method, action, url, success, $form = this;

    if (typeof options == 'function') {
        options = { success: options };
    }
    else if ( options === undefined ) {
        options = {};
    }
    success = options.success;
    method = options.type || this.attr2('method');
    action = options.url  || this.attr2('action');

    url = (typeof action === 'string') ? $.trim(action) : '';
    url = url || window.location.href || '';
    if (url) {
        url = (url.match(/^([^#]+)/)||[])[1];
    }
    var dataArr ;
    if (options.textOnly && options.textOnly == true) {
    	dataArr = $("text", $form).not("[onlyShow]").serializeArray();
    } else {
    	dataArr = $(":input", $form).not("[onlyShow]").serializeArray();
    }
    
	 var data = '{';
	 for (var i in dataArr){
		 if (options.exclude && dataArr[i].name.indexOf(options.exclude)>=0 ) {
			 continue;
		 }
		 if (!dataArr[i].value || dataArr[i].value == '') {
			 continue;
		 }
		 data +=   '"' + dataArr[i].name + '":' + $.toJSON(dataArr[i].value) + ',';
	 }
	 data = data.substring(0,data.length-1) + '}';
	 if (options.beforeSubmit && typeof options.beforeSubmit == 'function') {
		 // 调用前先将json转为对象，以便于调用方增加或者删除参数，处理后再转为json字符串
		 try{
				 var a = $.secureEvalJSON(data);
				 data = $.toJSON(options.beforeSubmit(a));
		 	}catch(e){
		 		bootbox.alert('JS脚本出错，请换其他客户端尝试!');
		 		console.error(e);
		 		return;
		 	}
	 }
	 
    options = jQuery.extend(true, {
        url:  url,
        type: method || 'post',
        data : data
    		}, options);

    return $.wedoAjax(options);
};
/**
 * 同步获取url数据
 */
$.getHttpData = function(url) {
    var result;
    $.wedoAjax({
        type: 'get',
        url: url,
        async : false,
        success: function(data) {
            result = data.data;
         }
    });
    return result;
}
/**
 * 获取上传文件url
 */
$.getFileUploadUrl = function (callback) {
    var url =  _webRoot + '/rest/fs/url/upload';
    $.ajax({
        url: url,
        type: 'get',
        success : function(responseUrl) {
            if (!responseUrl || responseUrl.indexOf("upToken") < 0) {
                throw new Error("获取上传路径出错")
            }
            callback(responseUrl);
        }
    });
}
})(jQuery);