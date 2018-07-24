<%@ page import="org.springframework.security.web.savedrequest.DefaultSavedRequest" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%
    DefaultSavedRequest savedRequest = null;
    if (session.getAttribute("SPRING_SECURITY_SAVED_REQUEST") != null)
        savedRequest = (DefaultSavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
%>

<o:header title="Log In"/>
<link rel="stylesheet" href="static/css/app.css" rel="stylesheet">

<%--
<script type="text/javascript">
    <!--

    $(document).ready(function () {
        // select the appropriate field based on context
        $('#<c:out value="${ login_hint != null ? 'password' : 'username' }" />').focus();
    });

    //-->
</script>
--%>
<%--<o:topbar />--%>
<div class="container-fluid">
    <div class="row-fluid">
        <div id="errors" class="span8 offset2">
            <c:if test="${ param.error != null }">
                <div class="alert alert-error"><spring:message code="login.error"/></div>
            </c:if>
        </div>
    </div>
</div>
<div class="container-fluid full-screen login-form">

    <div class="row-fluid">
        <div class="span4 offset4">

            <form id="createUserForm" name="createUserForm" action="<%=request.getContextPath()%>/account"
                  method="POST">
                <div>
                    <div class="input-prepend input-block-level">
                        <span class="add-on"><i class="icon-user"></i></span>
                        <input type="text" placeholder="User Id" autocorrect="off" autocapitalize="off" required
                               autocomplete="off" spellcheck="false" />"
                               id="userid" name="userid">
                    </div>
                </div>
                <div>
                    <div class="input-prepend input-block-level">
                        <span class="add-on"><i class="icon-mail"></i></span>
                        <input type="text" placeholder="Email Address" autocorrect="off" autocapitalize="off" required
                               autocomplete="off" spellcheck="false" />"
                               id="email" name="email">
                    </div>
                </div>
                <div>
                    <div class="input-prepend input-block-level">
                        <span class="add-on"><i class="icon-tag"></i></span>
                        <input type="text" placeholder="Given Name" autocorrect="off" autocapitalize="off" required
                               autocomplete="off" spellcheck="false" />"
                        id="givenName" name="givenName">
                    </div>
                </div>
                <div>
                    <div class="input-prepend input-block-level">
                        <span class="add-on"><i class="icon-tag"></i></span>
                        <input type="text" placeholder="Surname" autocorrect="off" autocapitalize="off" required
                               autocomplete="off" spellcheck="false" />"
                        id="surname" name="surname">
                    </div>
                </div>
                <div>
                    <div class="input-prepend input-block-level">
                        <span class="add-on"><i class="icon-tag"></i></span>
                        <input type="text" placeholder="Display Name" autocorrect="off" autocapitalize="off" required
                               autocomplete="off" spellcheck="false" />"
                        id="displayName" name="displayName">
                    </div>
                </div>
                <div>
                    <div class="input-prepend input-block-level">
                        <span class="add-on"><i class="icon-lock"></i></span>
                        <input type="password" placeholder="Password" autocorrect="off" autocapitalize="off" required
                               autocomplete="off" spellcheck="false" id="password" name="password">
                    </div>
                </div>
                <div>
                    <input id="submitButton" type="submit" class="btn-block login-form-btn" value="Create Account" name="submit">
                </div>
            </form>
        </div>
        <!-- a little space -->
        <div class="span12" style="height:50px;"></div>
    </div>
</div>
<script type="text/javascript">
    var personaAttemptFailed = false;

    $('#loginForm').submit(function (e) {
        var username = $('#username').val();
        var password = $('#password').val();

        if (username && username.length && looksLikePersona(username)) {
            e.preventDefault();
            var personaAuthEndpoint = "${config.personaAuthUrl}";

            $.ajax({
                type: "POST",
                url: personaAuthEndpoint,
                data: JSON.stringify({
                    "username": username,
                    "password": password
                }),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: personaAuthResponse,
                error: personaAuthFailure
            });
        }
    });

    function looksLikePersona(username){
        var personaRegex = /^[a-zA-Z0-9]{1,50}@[a-zA-Z0-9]{1,20}$/;
        return !personaAttemptFailed && username.match(personaRegex) !== null;
    }

    function personaAuthResponse(response) {
        if (!response.jwt || response.jwt.length < 1) {
            updateErrorMessage("Error authenticating persona user.");
            return;
        }
        // add persona cookie & remove session cookie
        document.cookie = "hspc-persona-token=" + response.jwt + "; expires=" + (new Date((new Date()).getTime() + 3 * 60000)).toUTCString() + "; Path=/;";
        // forward to authorize endpoint
        var redirectUrl = $('#redirectUrl').val();
        window.location = redirectUrl + "&cache=clear";
    }

    function personaAuthFailure(response) {
        // persona auth failed, try logging in as a real user
        personaAttemptFailed = true;
        $('#submitButton').click();
    }

    function updateErrorMessage(message) {
        if ($('div.alert').length)
            $('div.alert').remove();

        $('#errors').append("<div class='alert alert-error'>" + message + "</div>");
    }
</script>