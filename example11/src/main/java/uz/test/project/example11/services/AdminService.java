package uz.test.project.example11.services;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import uz.test.project.example11.dto.Role;
import uz.test.project.example11.dto.RoleToUserDto;
import uz.test.project.example11.utils.JSend;

@Slf4j
@Service
public class AdminService {
    private final RestTemplate restTemplate;
    @Value("${auth.server.base.url}")
    private String baseUrl;

    public AdminService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public JSend addRole(String role) {
        log.info("Sending request for new role");
        if (role != null) {
            if (ObjectUtils.isEmpty(role))
                return JSend.error("role field is empty");
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Content-Type", "application/json");
            headers.add("lang", "en");
            Role newRole = new Role(role);
            newRole.setId(null);
            HttpEntity request = new HttpEntity<>(role, headers);
            JsonNode response = restTemplate.postForObject(baseUrl + "/role/add/to/user", request, JsonNode.class);
            return JSend.success(response);
        }
        return JSend.error("Role is empty");
    }

    public JSend addRoleToUser(String username, String role) {
        if (role != null && username != null) {
            if (ObjectUtils.isEmpty(username) || ObjectUtils.isEmpty(role))
                return JSend.error("The required fields are empty");
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Content-Type", "application/json");
            headers.add("lang", "en");
            RoleToUserDto roleToUserDto = new RoleToUserDto(username, role);
            HttpEntity request = new HttpEntity(roleToUserDto, headers);
            JsonNode response = restTemplate.postForObject(baseUrl + "/role/save", request, JsonNode.class);
            return JSend.success(response);
        }
        return JSend.error("The username and role cannot be null");
    }
}
