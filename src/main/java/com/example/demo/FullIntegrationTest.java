package com.example.demo;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.util.JwtUtil;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.*;

import java.time.LocalDate;
import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FullIntegrationTest extends AbstractTestNGSpringContextTests {

    @LocalServerPort
    private int port;

    @Autowired private TestRestTemplate restTemplate;
    @Autowired private VendorRepository vendorRepository;
    @Autowired private DepreciationRuleRepository ruleRepository;
    @Autowired private AssetRepository assetRepository;
    @Autowired private AssetLifecycleEventRepository eventRepository;
    @Autowired private AssetDisposalRepository disposalRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    private static Long createdVendorId;
    private static Long createdRuleId;
    private static Long createdAssetId;
    private static Long adminUserId;
    private static Long normalUserId;

    private static String adminToken;
    private static String userToken;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    // ========================= SETUP =========================
    @BeforeClass
    public void setupData() {

        // ROLES
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ADMIN")));

        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(new Role("USER")));

        // ADMIN USER
        User admin = userRepository.findByEmail("integration_admin@example.com")
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail("integration_admin@example.com");
                    u.setName("IntegrationAdmin");
                    u.setPassword(passwordEncoder.encode("adminpass"));
                    return userRepository.save(u);
                });
        admin.getRoles().add(adminRole);
        adminUserId = admin.getId();

        // NORMAL USER
        User normal = userRepository.findByEmail("integration_user@example.com")
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail("integration_user@example.com");
                    u.setName("IntegrationUser");
                    u.setPassword(passwordEncoder.encode("userpass"));
                    return userRepository.save(u);
                });
        normal.getRoles().add(userRole);
        normalUserId = normal.getId();

        // TOKENS
        adminToken = jwtUtil.generateToken(
                admin.getEmail(),
                admin.getId(),
                Set.of("ADMIN")
        );

        userToken = jwtUtil.generateToken(
                normal.getEmail(),
                normal.getId(),
                Set.of("USER")
        );

        // VENDOR
        Vendor vendor = vendorRepository.findByVendorName("IntegrationVendor")
                .orElseGet(() -> {
                    Vendor v = new Vendor();
                    v.setVendorName("IntegrationVendor");
                    v.setContactEmail("vendor@example.com");
                    return vendorRepository.save(v);
                });

        createdVendorId = vendor.getId();

        // RULE
        DepreciationRule rule = ruleRepository.findByRuleName("IntegrationRule")
                .orElseGet(() -> {
                    DepreciationRule r = new DepreciationRule();
                    r.setRuleName("IntegrationRule");
                    r.setMethod("STRAIGHT_LINE");
                    r.setUsefulLifeYears(5);
                    r.setSalvageValue(10.0);
                    return ruleRepository.save(r);
                });

        createdRuleId = rule.getId();

        // ASSET
        List<Asset> assets = assetRepository.findByVendor(vendor);
        if (!assets.isEmpty()) {
            createdAssetId = assets.get(0).getId();
        } else {
            Asset a = new Asset();
            a.setAssetTag("INTEG-" + UUID.randomUUID());
            a.setAssetName("Integration Asset");
            a.setPurchaseDate(LocalDate.now().minusDays(10));
            a.setPurchaseCost(1000.0);
            a.setVendor(vendor);
            a.setDepreciationRule(rule);
            createdAssetId = assetRepository.save(a).getId();
        }
    }

    // ========================= BASIC TEST =========================
    @Test
    public void test_contextLoads() {
        Assertions.assertThat(port).isGreaterThan(0);
    }

    @Test
    public void test_rootAccessible() {
        ResponseEntity<String> resp = restTemplate.getForEntity(baseUrl() + "/", String.class);
        Assertions.assertThat(resp.getStatusCode().is2xxSuccessful()
                || resp.getStatusCode().is3xxRedirection()).isTrue();
    }

    // ========================= SIMPLE API TEST =========================
    @Test
    public void test_getAssets() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Asset[]> resp =
                restTemplate.exchange(baseUrl() + "/api/assets", HttpMethod.GET, entity, Asset[].class);

        Assertions.assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void test_getAssetById() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        ResponseEntity<Asset> resp =
                restTemplate.exchange(baseUrl() + "/api/assets/" + createdAssetId,
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        Asset.class);

        Assertions.assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
    }
}
