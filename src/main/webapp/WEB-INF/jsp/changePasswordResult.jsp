<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Result</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">

    <style>
        .result-icon {
            width: 80px;
            height: 80px;
            margin: 0 auto 30px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 48px;
        }

        .result-icon.success {
            background: #d4edda;
            color: #27ae60;
        }

        .result-icon.error {
            background: #f8d7da;
            color: #e74c3c;
        }

        .result-title {
            font-size: 28px;
            font-weight: 600;
            text-align: center;
            margin-bottom: 16px;
        }

        .result-title.success {
            color: #27ae60;
        }

        .result-title.error {
            color: #e74c3c;
        }

        .result-message {
            text-align: center;
            color: #666;
            font-size: 16px;
            line-height: 1.6;
            margin-bottom: 30px;
        }

        .action-btn {
            background: #4a4a4a;
            color: white;
            border: none;
            padding: 16px;
            border-radius: 12px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: background-color 0.3s ease;
            text-decoration: none;
            display: block;
            text-align: center;
        }

        .action-btn:hover {
            background: #333;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="form-container">
        <%-- Check if there's an error attribute --%>
        <% if (request.getAttribute("error") != null) { %>
            <div class="result-icon error">✗</div>
            <h2 class="result-title error">Operation Failed</h2>
            <p class="result-message">
                <%= request.getAttribute("error") %>
            </p>
        <% } else { %>
            <div class="result-icon success">✓</div>
            <h2 class="result-title success">Success!</h2>
            <p class="result-message">
                <%= request.getAttribute("message") != null ?
                    request.getAttribute("message") :
                    "Your password has been changed successfully." %>
            </p>
        <% } %>

        <%-- Optional: Add a button/link if provided --%>
        <% if (request.getAttribute("redirectUrl") != null) { %>
            <a href="<%= request.getAttribute("redirectUrl") %>" class="action-btn">
                <%= request.getAttribute("redirectLabel") != null ?
                    request.getAttribute("redirectLabel") :
                    "Continue" %>
            </a>
        <% } %>
    </div>
</div>

</body>
</html>
