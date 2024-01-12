package com.watson.monitor.communication;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

import java.io.IOException;
import java.util.List;

public class WSnmp {
    public static void main(String[] args) throws IOException {
        // SNMP Agent Configuration
        int agentPort = 161;
        String agentAddress = "udp:127.0.0.1/" + agentPort;
        Address agentTargetAddress = GenericAddress.parse(agentAddress);

        // SNMP Manager Configuration
        int managerPort = 161;
        String managerAddress = "udp:127.0.0.1/" + managerPort;
        Address managerTargetAddress = GenericAddress.parse(managerAddress);
        String community = "public"; // SNMP community string

        // SNMP Manager
        Snmp snmpManager = new Snmp(new DefaultUdpTransportMapping());
        snmpManager.listen();

        // SNMP Agent
        Snmp snmpAgent = new Snmp(new DefaultUdpTransportMapping());
        snmpAgent.listen();

        // Send a simple SNMP Get request
        sendSnmpGet(managerTargetAddress, community, "1.3.6.1.2.1.1.1.0", snmpManager);

        // Send a simple SNMP Set request
        sendSnmpSet(managerTargetAddress, community, "1.3.6.1.2.1.1.6.0", "New SysLocation", snmpManager);

        // Walk the SNMP tree
        walkSnmpTree(managerTargetAddress, community, "1.3.6.1.2.1", snmpManager);

        // Close SNMP Managers and Agents
        snmpManager.close();
        snmpAgent.close();
    }

    private static void sendSnmpGet(Address targetAddress, String community, String oid, Snmp snmp) throws IOException {
        // Create a PDU for the request
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oid)));

        // Set the PDU type to GET
        pdu.setType(PDU.GET);

        // Create the target
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version2c);

        // Send the request
        ResponseEvent response = snmp.send(pdu, target);

        // Process the response
        if (response != null && response.getResponse() != null) {
            PDU responsePDU = response.getResponse();
            System.out.println("SNMP Get Response: " + responsePDU.getVariableBindings());
        } else {
            System.out.println("SNMP Get Request failed.");
        }
    }

    private static void sendSnmpSet(Address targetAddress, String community, String oid, String value, Snmp snmp) throws IOException {
        // Create a PDU for the request
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oid), new OctetString(value)));

        // Set the PDU type to SET
        pdu.setType(PDU.SET);

        // Create the target
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version2c);

        // Send the request
        ResponseEvent response = snmp.send(pdu, target);

        // Process the response
        if (response != null && response.getResponse() != null) {
            PDU responsePDU = response.getResponse();
            System.out.println("SNMP Set Response: " + responsePDU.getVariableBindings());
        } else {
            System.out.println("SNMP Set Request failed.");
        }
    }

    private static void walkSnmpTree(Address targetAddress, String community, String baseOid, Snmp snmp) throws IOException {
        // Create the target
        UserTarget target = new UserTarget();
        target.setSecurityLevel(SecurityLevel.NOAUTH_NOPRIV);
        target.setSecurityModel(SecurityModel.SECURITY_MODEL_SNMPv2c);
        target.setSecurityName(new OctetString("public"));
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version2c);

        // Set up the TreeUtils for walking
        TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());

        // Perform the walk
        OID baseOidInstance = new OID(baseOid);
        List<TreeEvent> events = treeUtils.walk(target, new OID[]{baseOidInstance});

        // Process the walk results
        if (events == null || events.isEmpty()) {
            System.out.println("No SNMP response received.");
        } else {
            for (TreeEvent event : events) {
                if (event != null) {
                    VariableBinding[] vbs = event.getVariableBindings();
                    if (vbs != null) {
                        for (VariableBinding vb : vbs) {
                            System.out.println(vb);
                        }
                    }
                }
            }
        }
    }
}
