int main()
{
	/* Write 0x12 to address 0x234 */
	*((int*)0x234) = 0x12;

	return 0;
}
