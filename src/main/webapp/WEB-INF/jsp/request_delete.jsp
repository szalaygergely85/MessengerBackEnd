<%@ page contentType="text/html;charset=UTF-8" %>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account Deletion</title>
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
        <h2 class="form-title">Delete Account</h2>

        <form id="delete-request-form" class="form" action="${pageContext.request.contextPath}/delete-request"
              method="post">

            <div class="input-group">
                <input
                        type="email"
                        id="username"
                        name="username"
                        placeholder="Email Address"
                        required
                >
            </div>

            <div class="input-group">
                <input
                        type="password"
                        id="password"
                        name="password"
                        placeholder="Passord"
                        required
                >
                <button type="button" class="toggle-password" data-target="confirmPassword">
                    <svg width="20" height="20">
                        <use href="#eye-icon"/>
                    </svg>
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
            <button type="submit" class="submit-btn">Request Account Deletion</button>
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