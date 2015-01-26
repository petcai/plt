<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../inc/common.jsp"%>
<c:set var="title" value="课云" ></c:set>
<c:set var="keywords" value="课云教育" ></c:set>
<c:set var="description" value="在线教育 私人定制 知识图谱" ></c:set>
<%@ include file="../inc/header_new.jsp"%>
<script type="text/javascript">
var Globals = {};
Globals.ctx = "${ctx}";
Globals.lang = "${lang}";
Globals.page = "Index_weikeDetail";
</script>
</head>
<body>
	<div class="header">
		<%@ include file="../inc/top_new.jsp"%>
		<div class="con_quest">
			<div class="pinglun">
				<div class="ping_title">
					<%-- <h2>${luEntry.levelName}&nbsp;&nbsp;${luEntry.subjectName}&nbsp;&nbsp;${luEntry.termCnName}&nbsp;&nbsp;（${luEntry.unitName}）</h2> --%>
					<h2>${weike.courseName}</h2>
				</div>
				<div class="ping_left">
					<div class="ping_li">
						<video autoplay="autoplay" controls="controls" style="width:100%; height:100%; background:black;">
							<source src="${ctx}${weike.fileUrl}">
						</video>			
					</div>
				</div>
				<div class="ping_right">
					<h2>其他相关微课</h2>
					<c:forEach items="${weikeList}" var="otherWeike" varStatus="wt">
						<p>
							<a href="${ctx}/weike/${otherWeike.id}.html" target="_blank">${otherWeike.courseName}</a>
						</p>
					</c:forEach>
					<div class="right_but"><a href="${ctx}/war/exams?type=${type}&weikeId=${weike.id}" target="_blank">课后练习</a></div>
				</div>
			</div>
			<div class="clr"></div>
			<div class="ping_footer">
				<div class="ping_fo_ti">评论</div>
				
				<c:forEach items="${weike.commentList}" var="comment" varStatus="ct">
					<div class="ping_fo_co clearfix">
						<img src="${ctx}${comment.headPhoto}" width="80" height="80">
						<p>${comment.showName}</p>
						<p>${comment.remark}</p>
						<span class="pi_time"><fmt:formatDate value="${comment.cts}" type="both"/></span>
					</div>
				</c:forEach>
				<c:if test="${empty weike.commentList}"><div class="ping_fo_co clearfix"><p class="nothing">还没有评论，沙发等你来抢！</p></div></c:if>
				<div class="ping_comment_title">
					<div class="ping_say"><strong>我来说两句</strong></div>
		            <div class="ping_mr"><span class="pt3">请文明上网，理性发言！</span> 总共有&nbsp;&nbsp;<span class="pt5">${weike.commentCount}</span>条课程评论</div>
				</div>
				<div id="comment" class="ping_form">
					<form action="${ctx}/weike/comment" method="post" id="comment_form" class="common_form">
						<textarea name="remark" cols="" rows="" class="ping_tarea"></textarea>
						<!-- <div>
							<textarea id="xheditor_remark" name="remark" class="xheditor th_textarea" cols="2" rows="3"></textarea>
                            <script type="text/javascript">
							 	$('#xheditor_remark').xheditor({upImgUrl:'${ctx}/image/upload2', html5Upload:false, upMultiple:1, upImgExt:'jpg,jpeg,gif,png,bmp'});
							</script>
						</div> -->
						<input type="hidden" name="weikeId" value="${weike.id}"/>
						<input class="ping_submit" type="submit"  value="提交"/>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
