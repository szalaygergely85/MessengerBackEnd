<%@ page contentType="text/html;charset=UTF-8" %>



<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Forgot Password</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">


    <svg style="display:none">
        <symbol id="eye-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
            <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
            <circle cx="12" cy="12" r="3"></circle>
        </symbol>
    </svg>
</head>
<body>




<div class="container">

    <div class="form-container">
        <h2 class="form-title">Change Password</h2>

        <form id="changePasswordForm" class="form" action="changePassword">

            <input type="hidden" id="token" value="${token}">

            <div class="input-group">
                <input
                        type="password"
                        id="newPassword"
                        placeholder="New Password"
                        required
                >
                <button type="button" class="toggle-password" data-target="newPassword">
                    <svg width="20" height="20"><use href="#eye-icon"/></svg>
                </button>
            </div>

            <div class="input-group">
                <input
                        type="password"
                        id="confirmPassword"
                        placeholder="Confirm New Password"
                        required
                >
                <button type="button" class="toggle-password" data-target="confirmPassword">
                    <svg width="20" height="20"><use href="#eye-icon"/></svg>
                </button>
            </div>
            <!--
                        <div class="password-requirements">
                            <p>Password must contain:</p>
                            <ul>
                                <li id="length">At least 8 characters</li>
                                <li id="uppercase">One uppercase letter</li>
                                <li id="lowercase">One lowercase letter</li>
                                <li id="number">One number</li>
                            </ul>
                        </div>
-->
                        <button type="submit" class="submit-btn">Change Password</button>
                    </form>
                </div>
            </div>

            <script>
                document.querySelectorAll('.toggle-password').forEach(button => {
                    button.addEventListener('click', () => {
                        const inputId = button.getAttribute('data-target');
                        const input = document.getElementById(inputId);
                        input.type = (input.type === 'password') ? 'text' : 'password';
                    });
                });
            </script>
            </body>
            </html>