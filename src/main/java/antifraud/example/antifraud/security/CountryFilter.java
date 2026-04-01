package antifraud.example.antifraud.security;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.InetAddress;

@Component
@RequiredArgsConstructor
public class CountryFilter extends OncePerRequestFilter {

    private final DatabaseReader geoIpReader;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String ipAddress = getClientIp(request);

        try {
            InetAddress ip = InetAddress.getByName(ipAddress);
            CountryResponse country = geoIpReader.country(ip);

            if (!"UZ".equals(country.getCountry().getIsoCode())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access restricted to Uzbekistan.");
                return;
            }
            System.out.println("filter -> " + country.getCountry().getIsoCode());
        } catch (Exception e) {
        }

        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xf = request.getHeader("X-Forwarded-For");
        return (xf == null) ? request.getRemoteAddr() : xf.split(",")[0];
    }
}
