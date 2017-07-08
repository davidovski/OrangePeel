import discord
from discord import *
import asyncio
import time, json, datetime, collections

token = "MjUzMjc1Mzk3NDQ0MjA2NTk0.DCCBmg.ntbI6GayM4ea_HlBxcDLYUdwh0A"

def isAdmin(user):
    return user.id in admins

client = Client()


@client.event
async def on_ready():
    print('Logged in as')
    print(client.user.name)
    print(client.user.id)
    print('------')
    # for channel in client.get_all_channels():
    #     print(channel.name)
    # await background_task()


@client.event
async def on_message(message):
	if message.author.name == client.user.name:
		if message.content == "chutiya":
			members = []
		
			for member in message.server.members:
				for role in member.roles:
					if "chutiya" in role.name:
						members.append(member)
			print(str(len(members)))

        


client.run(token, bot=False)



