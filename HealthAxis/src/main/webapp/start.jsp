<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>スタート | 健診結果Web確認サービス</title>
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
        .start-box { 
            background: white; 
            padding: 40px; 
            border-radius: 8px; 
            box-shadow: 0 4px 12px rgba(0,0,0,0.1); 
            width: 320px; 
            text-align: center;
        }
        h1 { 
            margin-top: 0; 
            color: #333; 
            font-size: 22px;
            margin-bottom: 15px;
            font-weight: bold;
        }
        .subtitle {
            color: #666;
            font-size: 14px;
            margin-bottom: 30px;
        }
        .nav-btn { 
            display: block;
            padding: 14px; 
            background-color: #007bff; 
            color: white; 
            border: none; 
            border-radius: 4px; 
            font-size: 16px; 
            cursor: pointer; 
            font-weight: bold;
            text-decoration: none;
            transition: background-color 0.2s;
        }
        .nav-btn:hover { 
            background-color: #0056b3; 
        }
    </style>
</head>
<body>

    <div class="start-box">
        <h1>健診結果確認システム</h1>
        <p class="subtitle">下のボタンを押してログイン画面へお進みください</p>
        
        <!-- 💡一般ログインのボタンを配置し、踏んだら login.jsp に飛ぶようにしています -->
        <a href="login.jsp" class="nav-btn">一般ログイン</a>
    </div>

</body>
</html>

