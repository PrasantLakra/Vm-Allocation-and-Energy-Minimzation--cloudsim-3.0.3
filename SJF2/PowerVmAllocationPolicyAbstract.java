/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package SJF2;

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
			Log.formatLine("%.2f: No suitable host found for VM #" + vm.getId() + "\n", CloudSim.clock());
			return false;
		}
		if (host.vmCreate(vm)) { // if vm has been succesfully created in the host
			getVmTable().put(vm.getUid(), host);
			Log.formatLine(
					"%.2f: VM #" + vm.getId() + " has been allocated to the host #" + host.getId(),
					CloudSim.clock());
			return true;
		}
		Log.formatLine(
				"%.2f: Creation of VM #" + vm.getId() + " on the host #" + host.getId() + " failed\n",
				CloudSim.clock());
		return false;
	}

	
	// SJF
		public PowerHost findHostForVm(Vm vm) {
		    PowerHost allocatedHost = null;
		    double shortestCompletionTime = Double.MAX_VALUE;

		    for (PowerHost host : this.<PowerHost>getHostList()) {
		        if (host.isSuitableForVm(vm)) {
		            double completionTime = estimateCompletionTime1(host, vm);
		            
		            // Consider arrival time
		            double arrivalTime = vm.getArrivalTime();
		            if (CloudSim.clock() < arrivalTime) {
		                // If the VM hasn't arrived yet, skip it
		                continue;
		            }
		            
		            // Calculate waiting time
		            double waitingTime = Math.max(0, CloudSim.clock() - arrivalTime);
		            
		            // Include waiting time in completion time
		            completionTime += waitingTime;

		            if (completionTime < shortestCompletionTime) {
		                shortestCompletionTime = completionTime;
		                allocatedHost = host;
		            }
		        }
		    }

		    return allocatedHost;
		}


	protected abstract double estimateCompletionTime1(PowerHost host, Vm vm);

	protected abstract double estimateCompletionTime(PowerHost host, Vm vm);

	/**
	 * Find host for vm.
	 * 
	 * @param vm the vm
	 * @return the power host
	 *
	public PowerHost findHostForVm(Vm vm) {
		for (PowerHost host : this.<PowerHost> getHostList()) {
			if (host.isSuitableForVm(vm)) {
				return host;
			}
		}
		return null;
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
