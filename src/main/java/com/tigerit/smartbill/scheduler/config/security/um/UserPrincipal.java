package com.tigerit.smartbill.scheduler.config.security.um;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tigerit.smartbill.common.model.consts.enums.SystemUserRolePrivilegeNameENUM;
import com.tigerit.smartbill.common.model.dto.JwtCustomPayload;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@SuppressWarnings("serial")
@Data
public class UserPrincipal implements UserDetails {

    private Long systemUserId; //same as 'system_user'.'system_user_id' ?

    private String username;

    @JsonIgnore
    private String password;

    //#region [d3hint]
    private Integer systemUserTypeId;
    //#endregion [d3hint]

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long systemUserId,
                         //String name,
                         String username,
                         //String email,
                         String password,
                         Collection<? extends GrantedAuthority> authorities,
                         Integer systemUserTypeId

    ) {
        this.systemUserId = systemUserId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;

        this.systemUserTypeId = systemUserTypeId;
    }

    public static UserPrincipal create(JwtCustomPayload currentUser) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        List<Integer> items = currentUser.getAuthorities();
        for (int ii = 0; ii < items.size(); ii++) {
            authorities.add(new SimpleGrantedAuthority(SystemUserRolePrivilegeNameENUM.valueOfCode(items.get(ii)).getValue()));
        }
        return new UserPrincipal(
                currentUser.getSystemUserId(),
                currentUser.getUserName(),
                "",
                authorities,
                currentUser.getUserTypeId()
        );
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(systemUserId, that.systemUserId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(systemUserId);
    }
}