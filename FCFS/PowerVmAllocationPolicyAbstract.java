package FCFS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerHost;

public abstract class PowerVmAllocationPolicyAbstract extends VmAllocationPolicy {

    /** The vm table. */
    private final Map<String, Host> vmTable = new HashMap<String, Host>();

    /**
     * Instantiates a new power vm allocation policy abstract.
     * 
     * @param list the list
     */
    public PowerVmAllocationPolicyAbstract(List<? extends Host> list) {
        super(list);
    } 

    /*
     * (non-Javadoc)
     * @see org.cloudbus.cloudsim.VmAllocationPolicy#allocateHostForVm(org.cloudbus.cloudsim.Vm)
     */
    @Override
    public boolean allocateHostForVm(Vm vm) {
        return allocateHostForVm(vm, findHostForVm(vm));
    }

    /*
     * (non-Javadoc)
     * @see org.cloudbus.cloudsim.VmAllocationPolicy#allocateHostForVm(org.cloudbus.cloudsim.Vm,
     * org.cloudbus.cloudsim.Host)
     */
    @Override
    public boolean allocateHostForVm(Vm vm, Host host) {
        if (host == null) {
            Log.formatLine("%.2f: No suitable host found for VM #%d\n", CloudSim.clock(), vm.getId());
            return false;
        }
        if (host.vmCreate(vm)) { // if vm has been successfully created in the host
            getVmTable().put(vm.getUid(), host);
            Log.formatLine("%.2f: VM #%d has been allocated to the host #%d\n", CloudSim.clock(), vm.getId(), host.getId());
            return true;
        }
        Log.formatLine("%.2f: Creation of VM #%d on the host #%d failed\n", CloudSim.clock(), vm.getId(), host.getId());
        return false;
    }

 // FCFS
    public PowerHost findHostForVm(Vm vm) {
        PowerHost allocatedHost = null;
        for (PowerHost host : this.<PowerHost>getHostList()) {
            if (host.isSuitableForVm(vm)) {
                allocatedHost = host;
                break;
            }
        }
        return allocatedHost;
    }

    /*
     * (non-Javadoc)
     * @see org.cloudbus.cloudsim.VmAllocationPolicy#deallocateHostForVm(org.cloudbus.cloudsim.Vm)
     */
    @Override
    public void deallocateHostForVm(Vm vm) {
        Host host = getVmTable().remove(vm.getUid());
        if (host != null) {
            host.vmDestroy(vm);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.cloudbus.cloudsim.VmAllocationPolicy#getHost(org.cloudbus.cloudsim.Vm)
     */
    @Override
    public Host getHost(Vm vm) {
        return getVmTable().get(vm.getUid());
    }

    /*
     * (non-Javadoc)
     * @see org.cloudbus.cloudsim.VmAllocationPolicy#getHost(int, int)
     */
    @Override
    public Host getHost(int vmId, int userId) {
        return getVmTable().get(Vm.getUid(userId, vmId));
    }

    /**
     * Gets the vm table.
     * 
     * @return the vm table
     */
    public Map<String, Host> getVmTable() {
        return vmTable;
    }
}
