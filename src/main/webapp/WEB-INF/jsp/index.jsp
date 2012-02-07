<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Bracket App</title>
    <link rel="stylesheet" type="text/css" href="resources/css/style.css">
    <script data-main="resources/js/init" src="resources/js/lib/base/require-min.js"></script>
</head>
<body>


    <header>
        <h4>Bracket App</h4>
    </header>


    <div id="main" role="main">
        <div id="bracketNode"></div>
    </div>


    <footer></footer>


<c:if test="${not empty account.firstName}">
    <p>Welcome, <c:out value="${account.firstName}"/>!</p>
    <a href="<c:url value="/signout" />">Sign Out</a>

    <ul>
        <li><a href="connect/twitter">Twitter</a> (Connected? <c:out value="${twitter_status}"/>)</li>
    </ul>
</c:if>

<form id="twitter_signin" action="/bracket/signin/twitter" method="post">
    <button type="submit" style="height: 26px; width: 150px;background: transparent url('resources/social/twitter/sign-in-with-twitter-d.png') no-repeat center top;"></button>
</form>

</body>
</html>