<%-- 
    Author     : Lucía Fernández Florencio
--%>

<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <c:import url="/INC/headTags.jsp"/>
        <title>404 - ¡Ups! | Cacharreo</title>
        <link rel="stylesheet" type="text/css" href="${style}" /> 
        <style>
            .error-wrapper {
                text-align: center;
                max-width: 700px;
                margin: 0 auto;
                padding: 3rem 1.5rem;
            }

            .error-title {
                font: var(--text-h4);
                color: var(--color-darkest-lilac);
                margin-bottom: 0.5rem;
            }

            .error-subtitle {
                font: var(--font-text-xl);
                color: var(--color-lilac);
            }

            .error-img-container {
                margin: 0;
                display: flex;
                justify-content: center;
                align-items: center;
            }

            .error-image {
                max-width: 550px;
                width: 100%;
                height: auto;
                transition: transform 0.3s ease;
            }

            .error-image:hover {
                transform: rotate(-2deg) scale(1.02);
            }

            /* Ajuste para que el main de error se vea bien centrado */
            .main-error {
                justify-content: center !important;
                min-height: 80vh;
                margin-top:0;
                background: white;
            }
        </style>
    </head>
    <body> 
        <main class="main-error">
            <section class="error-wrapper">

                <h1 class="error-title">ERROR 404 <br>¡Ups! Parece que el becario perdi&oacute; lo que buscabas</h1>
                <p class="error-subtitle">Vuelve m&aacute;s tarde</p>

                <div class="error-img-container">
                    <img src="${context}/IMG/error404.jpg" 
                         alt="Becario Cacharreo perdido" 
                         class="error-image">
                </div>

                <div>
                    <a href="${context}/FrontController">
                        <button type="button" class="btn btn-primary px-3">
                            <i class="fa fa-shopping-cart me-2" aria-hidden="true"></i>
                            Volver a la tienda
                        </button>
                    </a>
                </div>

            </section>
        </main>
    </body>
</html>