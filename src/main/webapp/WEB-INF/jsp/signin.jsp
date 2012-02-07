<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ page session="false" %>
<html>
<head>
    <title>Spring Social Showcase: Sign In</title>
</head>
<body>
<h1>Spring Social Showcase: Sign In</h1>

<form id="signin" action="<c:url value="/signin/authenticate" />" method="post">
    <div class="formInfo">
        <c:if test="${signinError}">
            <div class="error">
                Your sign in information was incorrect.
                Please try again or <a href="<c:url value="/signup" />">sign up</a>.
            </div>
        </c:if>
    </div>

    <p>Some test user/password pairs you may use are:</p>
    <ul>
        <li>habuma/freebirds</li>
        <li>kdonald/melbourne</li>
        <li>rclarkson/atlanta</li>
    </ul>
</form>

</body>
</html>
