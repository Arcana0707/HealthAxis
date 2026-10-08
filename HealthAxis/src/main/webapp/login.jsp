<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ログイン | 健診結果Web確認サービス</title>
    <style>
        body { 
            font-family: sans-serif; 
            display: flex; 
            justify-content: center; 
            align-items: center; 
            height: 100vh; 
            margin: 0; 
            background-color: #f5f7fa; 
        }
        .login-box { 
            background: white; 
            padding: 40px; 
            border-radius: 8px; 
            box-shadow: 0 4px 12px rgba(0,0,0,0.1); 
            width: 320px; 
        }
        /* 💡ご指定通り、ヘッダー1にログインと配置し、スタイルを洗練させました */
        h1 { 
            margin-top: 0; 
            color: #333; 
            text-align: center; 
            font-size: 24px;
            margin-bottom: 25px;
            font-weight: bold;
        }
        .input-group { 
            margin-bottom: 20px; 
        }
        label { 
            display: block; 
            margin-bottom: 8px; 
            color: #666; 
            font-size: 14px; 
        }
        input[type="text"], input[type="password"] { 
            width: 100%; 
            padding: 10px; 
            border: 1px solid #ccc; 
            border-radius: 4px; 
            box-sizing: border-box; 
        }
        button { 
            width: 100%; 
            padding: 12px; 
            background-color: #007bff; 
            color: white; 
            border: none; 
            border-radius: 4px; 
            font-size: 16px; 
            cursor: pointer; 
            font-weight: bold;
            margin-top: 10px;
        }
        button:hover { 
            background-color: #0056b3; 
        }
        .error-msg {
            color: #dc3545;
            font-size: 13px;
            margin-bottom: 15px;
            text-align: center;
            display: none;
        }
    </style>
</head>
<body>

    <div class="login-box">
        <!-- 💡ヘッダー1（h1）にログインを設定 -->
        <h1>ログイン</h1>
        
        <!-- サーブレット側からエラーメッセージが帰ってきた場合に表示するエリア -->
        <% if (request.getAttribute("errorMsg") != null) { %>
            <div style="color: #dc3545; font-size: 13px; margin-bottom: 15px; text-align: center;">
                <%= request.getAttribute("errorMsg") %>
            </div>
        <% } %>
        <div id="js-error" class="error-msg"></div>

        <!-- 💡司令塔「UserLoginServlet」へデータを安全にPOST送信します -->
        <form action="LoginServlet" method="POST" onsubmit="return validateForm()">
            <!-- 💡上から順に従業員番号の入力欄 -->
            <div class="input-group">
                <label for="employee-id">従業員番号：</label>
                <input type="text" id="employee-id" name="employeeId" placeholder="従業員番号"required>
            </div>
            
            <!-- 💡次にパスワードの入力欄 -->
            <div class="input-group">
                <label for="password">パスワード：</label>
                <input type="password" id="password" name="password" placeholder="パスワード"required>
            </div>
            
            <!-- 💡一番下にログインボタン -->
            <button type="submit">ログイン</button>
        </form>
    </div>

    <script>
        // 💡未入力で送信ボタンが押された場合にブラウザ側で優しく止めるガード機能です
        function validateForm() {
            const empId = document.getElementById('employee-id').value.trim();
            const password = document.getElementById('password').value.trim();
            const errorDiv = document.getElementById('js-error');
            
            if (empId === "" || password === "") {
                errorDiv.innerText = "従業員番号とパスワードを入力してください。";
                errorDiv.style.display = "block";
                return false;
            }
            return true;
        }
    </script>
</body>
</html>
