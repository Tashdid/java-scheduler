/*
package com.tigerit.smartbill.scheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class CustomErrorPageController implements ErrorController {

    //private static final String ERROR_FILE_NAME = "custom_error_page";
    private static final String PATH = "/error";

    private ErrorAttributes errorAttributes;

    @Autowired
    public void setErrorAttributes(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }


    */
/*@RequestMapping(PATH)
    public ModelAndView handleError() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(ERROR_FILE_NAME);

        //modelAndView.addObject(errorAttributes);

        return modelAndView;
    }*//*


    //ref: [https://www.logicbig.com/tutorials/spring-framework/spring-boot/error-attributes.html]
    @RequestMapping(PATH)
    @ResponseBody
    public String handleError(HttpServletRequest request) {
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(servletWebRequest, true);
        final StringBuilder errorDetails = new StringBuilder();
        errorAttributes.forEach((attribute, value) -> {
            errorDetails.append("<tr><td>")
                    .append(attribute)
                    .append("</td><td><pre>")
                    .append(value)
                    .append("</pre></td></tr>");
        });

        return String.format("<html><head><style>td{vertical-align:top;border:solid 1px #666;}</style>"
                + "</head><body><h2>Error Page</h2><table>%s</table></body></html>", errorDetails.toString());
    }

    @RequestMapping("/generate_error")
    public void tempErrorGeneration() {
        throw new RuntimeException("a generated fake exception");
    }
}
*/
