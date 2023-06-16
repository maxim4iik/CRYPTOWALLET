pragma solidity ^0.8.0;
contract CryptowalletWithdrawal {
    address private owner;
    mapping(address => uint256) private balances;
    constructor() {
        owner = msg.sender;
    }
    modifier onlyOwner() {
        require(msg.sender == owner, "Only the contract owner can call this function.");
        _;
    }
    function deposit() external payable {
        balances[msg.sender] += msg.value;
    }
    function withdraw(uint256 amount) external {
        require(amount > 0, "Withdrawal amount must be greater than zero.");
        require(amount <= balances[msg.sender], "Insufficient balance.");
        balances[msg.sender] -= amount;
        payable(msg.sender).transfer(amount);
    }
    function getBalance() external view returns (uint256) {
        return balances[msg.sender];
    }
}