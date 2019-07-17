<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    Hello:${name}
    <hr>
    <#list citys?keys as key>
        <#if key == 'bj'>
            <li style="color: red">${key}——>${citys[key]}</li>
            <#else>
                <li style="color: green">${key}——>${citys[key]}</li>
        </#if>
    </#list>
</body>
</html>