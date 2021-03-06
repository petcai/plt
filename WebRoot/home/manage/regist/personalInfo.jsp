<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../inc/common.jsp"%>
<c:set var="title" value="三简在线教育平台" ></c:set>
<c:set var="keywords" value="456" ></c:set>
<c:set var="description" value="789" ></c:set>
<%@ include file="../../inc/header.jsp"%>
<script type="text/javascript">
var Globals = {};
Globals.ctx = "${ctx}";
Globals.lang = "${lang}";
Globals.page = "Manage_personalInfo";
</script>
<style type="text/css">
.system_tip { width:350px; line-height:26px; margin:10px auto; margin-left:100px; border:solid 1px #F5D8A7; border-radius:2px; background-color:#FFF6DB;padding-left: 10px;  }
</style>
</head>
<body>
	<%@ include file="../../inc/top_bg.jsp"%>
	<div id="container_box">
		<div id="container_content">
			<div class="sectionMain clr">
				<%@ include file="../../inc/leftmenu_regist.jsp"%>
				<div class="sh_info_r">
		            <div class="st_titile_r sh_title">
		            	<a href="${ctx}/account/personalInfo">基本信息</a>
		            	<a href="${ctx}/account/personalInfoEdu">教育信息</a>
		            	<a href="${ctx}/account/personalInfoExtend">其他扩展信息</a>
		            </div>
		            <div class="sh_form_con">
		                <div id="demo_zone">
		                	<c:if test="${tipError != null && fn:length(tipError)  > 0}">
								<div class="system_tip">
									<c:forEach items="${tipError}" var="tip">
									<p>${tip.msg}</p>
									</c:forEach>
								</div>
							</c:if>
		                    <form method="post" action="${ctx}/account/personalInfo" class="form_info common_form" id="personalInfo_form">
		                    	<input type="hidden" name="id" value="${user.id}" />
		                        <div class="form_item">
		                            <label for="username">昵称：</label>
		                            <div class="form_field">
		                                <input placeholder="6-32个字符" class="form_text" id="username" name="username" type="hidden" value="${user.username}"/>
		                                <strong>${user.username}</strong>
		                               <!--  <span class="not-empty" title='此项为必填项'>*</span> -->
		                                <span class="form_error">（此项为登录名）</span>
		                            </div>
		                        </div>
		                        <div class="form_item">
		                            <label>性别：</label>
		                            <div class="form_field">
	                                    <input <c:if test="${user.gender == 1}"> checked="checked" </c:if> value="1" class="form_radio" name="gender" type="radio">
	                                    <span>男</span>
	                                    <input <c:if test="${user.gender == 2}"> checked="checked" </c:if> value="2" class="form_radio" name="gender" type="radio">
	                                    <span>女</span>
	                                    <span class="not-empty" title='此项为必填项'>*</span>
		                            </div>
		                        </div>
		                        <div class="form_item">
		                            <label for="showName">真实姓名：</label>
		                            <div class="form_field">
		                                <input placeholder="填写您的真实姓名" class="form_text" id="showName" name="showName" type="text" value="${user.showName}" required/>
		                                <span class="not-empty" title='此项为必填项'>*</span>
		                                <span class="form_error">（请填写个人的真实姓名）</span>
		                            </div>
		                        </div>
		                        <div class="form_item">
		                            <label>身份证号码：</label>
		                            <div class="form_field ">
		                               <input placeholder="请填入您的真实身份证号码" class="form_text" id="idCard" name="idCard" type="text" value="${user.idCard}" required_Idcard>
		                               <span class="form_error">（真实身份证号码为15数字或者18位数字或者17位数字+X）</span>
		                            </div>
		                        </div>  
		                        <div class="form_item">
		                            <label for="phone">手机号：</label>
		                            <div class="form_field">
		                                <input placeholder="请填入您的手机号码" class="form_text" id="phone" name="phone" type="text" value="${user.phone}" required_Phone>
		                                <span class="form_error">（手机号码为11位数字）</span>
		                            </div>
		                        </div>
		                        <div class="form_item">
		                            <label for="email">邮箱：</label>
		                            <div class="form_field">
		                            <c:if test="${user.email==null}">
		                            	<input placeholder="请填入您的邮箱" class="form_text" id="email" name="email" type="email" value="${user.email}" style="">
		                            </c:if>
		                                <input placeholder="请填入您的邮箱" class="form_text" id="email" name="email" type="email" value="${user.email}" required required_Mail>
		                                <span class="not-empty" title='此项为必填项'>*</span>
		                                <span class="form_error">（邮箱格式，如：xxx@qq.com,xxx@163.com...）</span>
		                            </div>
		                        </div>
								<div class="form_item">
										<label for="url">上传头像：</label>
										<div class="form_field">
											<img src="${user.headPhoto}" style="width:80px; height:60px;border:1px #ccc solid;" class="small_preview" />
											<a href="javascript:void(0);" id="changeCover">更改头像</a>
											<input type="hidden" id="url" name="url" value="${user.url}" />
											<span class="form_error">（图片大小不允许超过300K）</span>
										</div>
									</div>                     
		                        <div class="form_item">
		                            <label>个人简介：</label>
		                            <div class="form_field">
		                                <textarea id="xheditor_remark" name="remark" class="xheditor-mfull xheditor {skin:'o2007silver'}"  style="width:85%;height:230px;">${user.remark}</textarea>
		                            </div>
		                        </div>
		                        <div class="form_action"><input type="submit" value="保存" /></div>
		                    </form>
		                </div>
		            </div>
		        </div>
			</div>
		</div>
	</div>

	<%@ include file="../../inc/friendlylink.jsp"%>
</body>
</html>