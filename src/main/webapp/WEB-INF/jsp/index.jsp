<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- Prevent the creation of a session --%>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>The Bracket App</title>
    <link rel="stylesheet" type="text/css" href="resources/css/reset.css">
    <link rel="stylesheet" type="text/css" href="resources/css/style.css">
    <link rel="stylesheet" type="text/css" href="resources/css/application.css">
    <link rel="stylesheet" type="text/css" href="resources/css/smoothness/jquery-ui-1.8.18.custom.css">

    <!-- Use Google Web Fonts -->
    <link href='' rel='stylesheet' type='text/css'>
    <script data-main="resources/js/init" src="resources/js/lib/base/require-min.js"></script>
    <script type="text/javascript">
        require.config({
            baseUrl: "/bracket/resources/js/",
            paths: {
                "html": "/bracket/resources/html"
            }
        });
        <!--google analytics-->
        var _gaq = _gaq || [];
        _gaq.push(['_setAccount', 'UA-29943111-1']);
        _gaq.push(['_trackPageview']);

        (function() {
            var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
            ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
            var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
        })();

    </script>
</head>
<body>



    <header class="clear_fix">
        <h1>The Bracket <strong>App</strong></h1>
        <nav>
            <a href="#" class="navbar leaderboard hidden">Leaderboard</a>
        </nav>
        <p class="follow_us">
            Follow <a href="https://twitter.com/#!/thebracketapp">@thebracketapp</a> for updates.
        </p>
        <div class="login">
            <c:if test="${not empty account.displayName}">
                <p>Hello, <strong><c:out value="${account.displayName}"/></strong><br/>
                    <a href="<c:url value="/signout" />">Sign Out</a>
                </p>
                <p><img src="${account.profileUrl}" /> </p>

            </c:if>
            <c:if test="${empty account.displayName}">
                <form id="twitter_signin" action="/bracket/signin/twitter" method="post">
                    <input class="twitter_signin" type="submit" value="Sign in"/>
                </form>
            </c:if>
        </div>
    </header>

    <div id="leaderBoard"></div>
    <div id="main" role="main">
        <div id="bracketNode">
            <div id="scoreboard" class="hidden">
                <h2>MY SCOREBOARD</h2>
                    <ul class="stats">
                        <li>
                            <div class="title">POINTS</div>
                            <div class="value">30</div>
                        </li>
                        <li>
                            <div class="title">AVAILABLE</div>
                            <div class="value">180</div>
                        </li>
                        <li>
                            <div class="title">PLACE</div>
                            <div class="value">15</div>
                        </li>
                        <li>
                            <div class="title">OUT OF</div>
                            <div class="value">231</div>
                        </li>
                    </ul>
            </div>
        </div>
    </div>

    <div style="height:9em;width:100%;"></div>
    <div style="
        width: 100%;
        height:1.5em; background: -moz-linear-gradient(top, #6C6C6C 0%, #6C6C6C 0%, black 100%);
        background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #6C6C6C), color-stop(0%, #6C6C6C), color-stop(100%, black));
        background: -webkit-linear-gradient(top, #6C6C6C 0%, #6C6C6C 0%, black 100%);
        background: -o-linear-gradient(top, #6C6C6C 0%, #6C6C6C 0%, black 100%);
        background: -ms-linear-gradient(top, #6C6C6C 0%, #6C6C6C 0%, black 100%);
        background: linear-gradient(top, #6C6C6C 0%, #6C6C6C 0%, black 100%);
        color: #EFE9E5;
    ">
        <div style="float: right; width:100%; text-align: right; color:#EFE9E5; padding-top:3px;">
            <a style="color: #EFE9E5; padding-left: 5px; padding-right: 5px" href="https://twitter.com/#!/porvak">@porvak</a>
            <a style="color: #EFE9E5; padding-left: 5px; padding-right: 5px" href="https://twitter.com/#!/y3sh">@y3sh</a>
            <a style="color: #EFE9E5; padding-left: 5px; padding-right: 15px" href="https://github.com/porvak/bracket">Fork me on GitHub</a>
        </div>
    </div>
</body>
</html>