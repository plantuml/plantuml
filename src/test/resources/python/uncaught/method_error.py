class Account:
    def __init__(self, balance):
        self.balance = balance

    def withdraw(self, amount):
        if amount > self.balance:
            raise ValueError("insufficient funds: " + str(self.balance))
        self.balance -= amount

a = Account(10)
a.withdraw(4)
print(a.balance)
a.withdraw(7)
