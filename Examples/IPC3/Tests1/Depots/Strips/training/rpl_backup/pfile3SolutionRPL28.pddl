9
(Unload hoist1 crate5 truck0 distributor0)
(Lift hoist1 crate0 pallet1 distributor0)
(Unload hoist1 crate4 truck0 distributor0)
(Drive truck0 distributor0 distributor1)
(Drop hoist1 crate4 pallet1 distributor0)
(Load hoist1 crate0 truck0 distributor0)
(Drop hoist1 crate5 crate0 distributor0)
(Unload hoist2 crate0 truck0 distributor1)
(Drop hoist2 crate0 crate1 distributor1)
1
(:init
	(on crate0 pallet1)
	(available hoist1)
	(in crate5 truck0)
	(on crate2 pallet0)
	(at truck0 distributor0)
	(in crate4 truck0)
	(on crate2 crate1)
	(at hoist0 depot0)
	(at pallet0 depot0)
	(available hoist2)
	(on crate1 pallet2)
	(clear crate3)
	(at crate0 distributor0)
	(at pallet1 distributor0)
	(at crate0 depot0)
	(at hoist1 distributor0)
	(at pallet2 distributor1)
	(clear crate0)
	(on crate0 pallet0)
	(at crate3 depot0)
	(on crate3 crate2)
	(at truck1 distributor0)
	(at crate1 distributor1)
	(at hoist2 distributor1)
	(available hoist0)
	(on crate3 pallet1)
	(clear crate1)
	(at crate2 depot0)
)
(:add
	(at truck0 distributor1)
	(available hoist1)
	(at crate4 distributor0)
	(on crate4 pallet1)
	(clear crate4)
	(lifting hoist1 crate0)
	(clear pallet1)
	(available hoist1)
	(on crate5 crate0)
	(clear crate5)
	(at crate5 distributor0)
	(available hoist1)
	(in crate0 truck0)
	(lifting hoist1 crate4)
	(lifting hoist2 crate0)
	(lifting hoist1 crate5)
	(on crate0 crate1)
	(clear crate0)
	(available hoist2)
	(at crate0 distributor1)
)
(:delete
	(at truck0 distributor0)
	(clear pallet1)
	(lifting hoist1 crate4)
	(available hoist1)
	(on crate0 pallet1)
	(clear crate0)
	(at crate0 distributor0)
	(clear crate0)
	(lifting hoist1 crate5)
	(lifting hoist1 crate0)
	(available hoist1)
	(in crate4 truck0)
	(in crate0 truck0)
	(available hoist2)
	(available hoist1)
	(in crate5 truck0)
	(lifting hoist2 crate0)
	(clear crate1)
)
(:goal
	(on crate5 crate0)
	(on crate0 crate1)
	(on crate4 pallet1)
	(on crate2 pallet0)
	(on crate1 pallet2)
	(on crate3 crate2)
)
(:current
	(on crate2 pallet0)
	(on crate1 pallet2)
	(on crate3 crate2)
)
