<%-- Prevent the creation of a session --%>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>The Bracket App</title>
    <link rel="stylesheet" type="text/css" href="resources/css/style.css">
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
    </script>
</head>
<body>


    <header>
        <h4>The Bracket App</h4>
    </header>


    <div id="main" role="main">
        <div id="bracketNode"></div>
    </div>


    <footer>
    </footer>


</body>
</html>