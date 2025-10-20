<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account Deletion Request</title>
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

        .result-icon.warning {
            background: #fff3cd;
            color: #f39c12;
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

        .result-title.warning {
            color: #f39c12;
        }

        .result-message {
            text-align: center;
            color: #666;
            font-size: 16px;
            line-height: 1.6;
            margin-bottom: 30px;
        }

        .warning-box {
            background: #fff3cd;
            border: 2px solid #f39c12;
            border-radius: 12px;
            padding: 16px;
            margin-bottom: 30px;
        }

        .warning-box p {
            color: #856404;
            margin: 0;
            font-size: 14px;
            line-height: 1.5;
        }

        .warning-box strong {
            display: block;
            margin-bottom: 8px;
            font-size: 16px;
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
            <h2 class="result-title error">Request Failed</h2>
            <p class="result-message">
                <%= request.getAttribute("error") %>
            </p>
            <a href="${pageContext.request.contextPath}/request-account-deletion" class="action-btn">
                Try Again
            </a>
        <% } else { %>
            <div class="result-icon warning">✉</div>
            <h2 class="result-title warning">Check Your Email</h2>
            <p class="result-message">
                <%= request.getAttribute("message") %>
            </p>

            <div class="warning-box">
                <strong>⚠ Important:</strong>
                <p>• The confirmation link will expire in 5 minutes</p>
                <p>• This action is permanent and cannot be undone</p>
                <p>• All your data, messages, and contacts will be permanently deleted</p>
                <p>• If you did not request this, you can safely ignore the email</p>
            </div>
        <% } %>
    </div>
</div>

</body>
</html>
