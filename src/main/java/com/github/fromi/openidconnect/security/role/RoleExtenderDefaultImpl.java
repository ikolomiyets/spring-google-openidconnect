package com.github.fromi.openidconnect.security.role;

import java.util.StringTokenizer;

public class RoleExtenderDefaultImpl implements RoleExtender {
    @Override
    public String extendRoles(String groups) {
//        StringBuilder result = new StringBuilder();
//        boolean firstItem = true;
//        StringTokenizer st = new StringTokenizer(groups, ",");
//        while (st.hasMoreTokens()) {
//            String group = st.nextToken();
//            if (group != null) {
//                if (!firstItem) {
//                    result.append(",");
//                } else {
//                    firstItem = false;
//                }
//
//                if (group.equalsIgnoreCase("ADMIN")) {
//                    result
//                            .append("ROLE_ADMINISTRATOR")
//                            .append(",")
//                            .append("ROLE_OPERATOR");
//                } else if (group.equalsIgnoreCase("CLERK")) {
//                    result.append("ROLE_OPERATOR");
//                } else {
//                    result.append(group);
//                }
//            }
//        }
//        return result.toString();
        return groups;
    }
}
