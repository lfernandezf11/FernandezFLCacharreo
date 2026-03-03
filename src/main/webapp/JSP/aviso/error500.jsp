<%-- 
    Author     : Lucía Fernández Florencio
--%>

<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <c:import url="/INC/headTags.jsp"/>
        <title>500 - ¡Catástrofe! | Cacharreo</title>
        <link rel="stylesheet" type="text/css" href="${style}" /> 
        <style>
            .error-wrapper {
                text-align: center;
                max-width: 700px;
                margin: 0 auto;
                padding: 3rem 1.5rem;
            }

            .main-error {
                display: flex;
                flex-direction: column;
                justify-content: center !important;
                min-height: 100vh;
                margin-top: 0;     
            }
            .error-img-container {
                border: 4px solid black;
            }
            
            .error-title {
                color: var(--color-dark-lilac);
            }
            
            .error-subtitle {
                color: var(--color-lilac);
            }
        </style>
    </head>
    <body> 
        <main class="main-error">
            <section class="error-wrapper">

                <h3 class="error-title text-uppercase fw-bold">Error 500</h3>
                <p class="error-subtitle fw-semibold">Parece que algo ha explotado en el servidor. Estamos recogiendo los trozos.</p>

                <div class="error-img-container">
                    <img src="${context}/IMG/error500.png" 
                         alt="Error de servidor Cacharreo" 
                         class="error-image">
                </div>
                         
                <div class="mt-4">
                    <p class="text-muted small mb-3">Intenta refrescar la página o vuelve más tarde.</p>
                    <a href="${context}/FrontController">
                        <button type="button" class="btn btn-primary px-4">
                            <i class="fa fa-refresh me-2" aria-hidden="true"></i>
                            Reintentar o ir al Inicio
                        </button>
                    </a>
                </div>

            </section>
        </main>
    </body>
</html>