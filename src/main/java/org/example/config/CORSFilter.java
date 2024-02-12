package org.example.config;



//public class CORSFilter implements Filter {
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        /** Invoked Do-filter method for send the requests & response to related servlets*/
//        filterChain.doFilter(servletRequest,servletResponse);
//        /** Cross Policy Header */
//        HttpServletResponse resp = (HttpServletResponse) servletResponse;
//        resp.setContentType("application/json");//MIME Type
//        //For Delete record
//        resp.addHeader("Access-Control-Allow-Origin", "*");
//        //For Update(PUT) record
//        resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
//        //For Delete & Update(PUT) records
//        resp.addHeader("Access-Control-Allow-Methods", "DELETE,PUT");
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//}