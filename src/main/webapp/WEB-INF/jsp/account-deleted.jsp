<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account Deletion</title>
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

        .info-box {
            background: #f8f9fa;
            border: 1px solid #e1e5e9;
            border-radius: 12px;
            padding: 20px;
            margin-bottom: 30px;
        }

        .info-box p {
            color: #666;
            margin: 0;
            font-size: 14px;
            line-height: 1.8;
        }

        .info-box strong {
            display: block;
            margin-bottom: 12px;
            font-size: 16px;
            color: #333;
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
            <h2 class="result-title error">Deletion Failed</h2>
            <p class="result-message">
                <%= request.getAttribute("error") %>
            </p>
            <a href="${pageContext.request.contextPath}/request-account-deletion" class="action-btn">
                Request New Deletion Link
            </a>
        <% } else { %>
            <div class="result-icon success">✓</div>
            <h2 class="result-title success">Account Deleted</h2>
            <p class="result-message">
                <%= request.getAttribute("message") %>
            </p>

            <div class="info-box">
                <strong>What happens next:</strong>
                <p>• All your personal information has been removed from our servers</p>
                <p>• Your messages and conversation history have been permanently deleted</p>
                <p>• Your contacts will no longer be able to find you on Zenvy</p>
                <p>• This action cannot be reversed</p>
            </div>

            <p style="text-align: center; color: #999; font-size: 14px; margin-top: 20px;">
                Thank you for using Zenvy. We're sorry to see you go.
            </p>
        <% } %>
    </div>
</div>

</body>
</html>
